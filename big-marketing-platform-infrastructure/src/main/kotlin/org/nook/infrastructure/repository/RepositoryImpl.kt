package org.nook.infrastructure.repository

import org.nook.domain.strategy.model.entity.StrategyAwardEntity
import org.nook.domain.strategy.model.entity.StrategyEntity
import org.nook.domain.strategy.model.entity.StrategyRuleEntity
import org.nook.domain.strategy.repository.IRepository
import org.nook.infrastructure.cache.RedisService
import org.nook.infrastructure.persistence.mapper.StrategyAwardMapper
import org.nook.infrastructure.persistence.mapper.StrategyMapper
import org.nook.infrastructure.persistence.mapper.StrategyRuleMapper
import org.nook.types.common.Constants
import org.nook.types.exception.AppException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.math.BigDecimal


@Repository
class RepositoryImpl(
    private val strategyAwardMapper: StrategyAwardMapper,
    private val strategyMapper: StrategyMapper,
    private val strategyRuleMapper: StrategyRuleMapper,
    private val redisService: RedisService
): IRepository {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(RepositoryImpl::class.java)
    }


    /**
     * 查询策略配置
     */
    override fun getStrategyAwardEntity(strategyId: Long): List<StrategyAwardEntity> {
        val cacheKey: String = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId

        // 1. 【第一步：尝试从缓存中获取值】
        return redisService.getValue<List<StrategyAwardEntity>>(cacheKey) ?.also{
            // 1a. 【缓存命中】
            log.info("缓存命中: {}", cacheKey)
        }?: run {
            // 2. 【第二步：缓存未命中，查询数据库】
            log.info("缓存未命中: {}", cacheKey)

            val awardPOs = strategyAwardMapper.getStrategyAwardByStrategyId(strategyId)
            if (awardPOs.isEmpty()) {
                // 2b. 【数据库未命中，返回空列表】
                mutableListOf()  // ← 改为可变 List
            } else {
                // 2a. 【数据库命中，写回缓存】
                val awardEntities = awardPOs.map { po -> po.toEntity() }
                awardEntities.also { entities ->
                    log.info("查询数据库成功，写回缓存: {}", cacheKey)
                    redisService.setValue(cacheKey, entities)
                }
            }
        }
    }


    /**
     * 查询Strategy Entity
     */
    override fun getStrategyEntity(strategyId: Long): StrategyEntity{
        val cacheKey: String = Constants.RedisKey.STRATEGY_KEY + strategyId

        // 1. 【第一步：尝试从缓存中获取值】
        return redisService.getValue<StrategyEntity>(cacheKey)?.also {
            // 1a. 【缓存命中】
            log.info("缓存命中: {}", cacheKey)
        } ?: run {
            // 2. 【第二步：缓存未命中，查询数据库】
            log.info("缓存未命中: {}", cacheKey)
            strategyMapper.getByStrategyId(strategyId)?.toEntity()?.also { dbEntity ->
                // 2a. 【数据库命中，写回缓存】
                log.info("数据库命中，写回缓存: {}", cacheKey)
                redisService.setValue(cacheKey, dbEntity)
            } ?: run {
                // 2b. 【数据库未命中，抛出异常】
                log.warn("数据库未命中, strategyId: {}", strategyId)
                throw AppException("STRATEGY_NOT_FOUND", "Strategy ID: $strategyId 不存在")
            }
        }
    }


    /**
     * 查询策略级规则（award_id = NULL, rule_type = 1）
     * 
     * 例如：rule_weight（权重规则）、rule_blacklist（黑名单规则）
     * 这些规则对整个策略生效，每个策略每种规则只有一条记录
     */
    override fun getType1StrategyRuleEntity(strategyId: Long, ruleModel: String): StrategyRuleEntity {
        val cacheKey: String = Constants.RedisKey.STRATEGY_RULE_KEY + strategyId + "_" + ruleModel

        // 1. 【第一步：尝试从缓存中获取值】
        return redisService.getValue<StrategyRuleEntity>(cacheKey)?.also {
            // 1a. 【缓存命中】
            log.info("缓存命中: {}", cacheKey)
        } ?: run {
            // 2. 【第二步：缓存未命中，查询数据库】
            log.info("缓存未命中: {}", cacheKey)
            strategyRuleMapper.getStrategyRuleByStrategyIdAndRuleModel(strategyId, ruleModel)?.toEntity()?.also { dbEntity ->
                // 2a. 【数据库命中，写回缓存】
                log.info("数据库命中，写回缓存: {}", cacheKey)
                redisService.setValue(cacheKey, dbEntity)
            } ?: run {
                // 2b. 【数据库未命中，抛出异常】
                log.warn("数据库未命中, strategyId: {}, ruleModel: {}", strategyId, ruleModel)
                throw AppException("STRATEGY_RULE_NOT_FOUND", "Strategy ID: $strategyId, Rule Model: $ruleModel 不存在")
            }
        }
    }

    override fun getStrategyRuleEntity(strategyId: Long, awardId: Int): StrategyRuleEntity {
        TODO("Not yet implemented")
    }

    override fun getStrategyRuleEntity(strategyId: Long, awardId: Int, ruleModel: String): StrategyRuleEntity {
        return strategyRuleMapper.getStrategyRuleByStrategyIdAndAwardIdAndRuleModel(strategyId,awardId,ruleModel)
            ?.toEntity() ?: throw AppException("STRATEGY_RULE_NOT_FOUND", "Strategy ID: $strategyId, Award ID: $awardId, Rule Model: $ruleModel 不存在")
    }


    /**
     * 存储策略概率查询表
     */
    override fun storeProbabilityTable(key: String, rateRange: BigDecimal, strategyAwardSearchTable: List<Int>) {
        // 存储概率范围
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange.toInt())

        // 存储概率查询表
        redisService
            .getMap<Int, Int>(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key)
            .putAll(strategyAwardSearchTable.mapIndexed { index, value -> index to value }.toMap())
        log.info("写入缓存: {}, rateRange: {}, tableContent{}", Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, rateRange, strategyAwardSearchTable)
    }


    /**
     * 获取概率范围
     */
    override fun getRateRange(strategyId: Long): Int =
        redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId)?: run {
            throw AppException("500", "Rate Range not found for strategyId: $strategyId")
        }

    override fun getRateRange(key: String): Int =
        redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key)?: run {
            throw AppException("500", "Rate Range not found for key: $key")
        }

    /**
     * 获取策略奖品id
     */
    override fun getAwardIdFromStrategyRateTable(strategyId: Long, random: Int): Int =
        redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, random)?: run {
            throw AppException("500", "Strategy Award Assemble not found for strategyId: $strategyId, random: $random")
        }

    override fun getAwardIdFromStrategyRateTable(key: String, random: Int): Int =
        redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, random)?: run {
            throw AppException("500", "Strategy Award Assemble not found for key: $key, random: $random")
        }



}