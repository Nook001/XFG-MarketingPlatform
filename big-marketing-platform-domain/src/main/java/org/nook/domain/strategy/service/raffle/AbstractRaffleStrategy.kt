package org.nook.domain.strategy.service.raffle

import org.nook.domain.strategy.model.raffle.RaffleAwardEntity
import org.nook.domain.strategy.model.raffle.RaffleInputEntity
import org.nook.domain.strategy.model.raffle.RuleActionEntity
import org.nook.domain.strategy.model.entity.StrategyEntity
import org.nook.domain.strategy.model.enums.RuleLogicCheckTypeVO
import org.nook.domain.strategy.repository.IRepository
import org.nook.domain.strategy.service.dispatcher.IStrategyDispatcher
import org.nook.domain.strategy.service.rule.RaffleFilterFactory
import org.nook.types.enums.ResponseCode
import org.nook.types.exception.AppException


/**
 * 抽奖策略抽象类
 */
abstract class AbstractRaffleStrategy(
    val repository: IRepository,
    val dispatcher: IStrategyDispatcher,
): IRaffleStrategy {


    override fun performRaffle(raffleInputEntity: RaffleInputEntity): RaffleAwardEntity {
        // 1. 输入参数校验
        val userId: String = raffleInputEntity.userId
        val strategyId: Long = raffleInputEntity.strategyId

        if (userId.isBlank()) {
            throw AppException(ResponseCode.ILLEGAL_PARAMETER.code, ResponseCode.ILLEGAL_PARAMETER.info)
        }

        // 2. 数据库 查询策略信息
        val strategy: StrategyEntity = repository.getStrategyEntity(strategyId)

        // 进行抽奖前规则过滤
        val ruleActionEntity: RuleActionEntity= doCheckRaffleBeforeFilter(
            raffleInputEntity,
            strategy.getRuleModelList()
        )

        // 3. 如果被规则拦截
        if (ruleActionEntity.code == RuleLogicCheckTypeVO.TAKE_OVER.code) {
            // 3-1 校验数据不为空
            if (ruleActionEntity.data == null) {
                throw AppException("Rule action data is null", ResponseCode.ILLEGAL_PARAMETER.code)
            }

            // 3-2 根据不同规则类型处理
            when (ruleActionEntity.ruleModel) {
                // 黑名单规则：直接返回兜底奖品
                RaffleFilterFactory.LogicModel.RULE_BLACKLIST.code -> {
                    val awardId = ruleActionEntity.data.awardId
                        ?: throw AppException("Blacklist rule awardId is null", ResponseCode.ILLEGAL_PARAMETER.code)
                    return RaffleAwardEntity(awardId = awardId)
                }

                // 权重规则：根据权重值从指定奖品池中抽取
                RaffleFilterFactory.LogicModel.RULE_WEIGHT.code -> {
                    val ruleWeightValueKey: String = ruleActionEntity.data.ruleWeightValueKey
                        ?: throw AppException("Rule weight value key is null", ResponseCode.ILLEGAL_PARAMETER.code)
                    val awardId: Int = dispatcher.getRandomAwardId(strategyId, ruleWeightValueKey)
                    return RaffleAwardEntity(awardId = awardId)
                }
            }
        }

        // 4. 抽奖
        val awardId = dispatcher.getRandomAwardId(strategyId)

        // 5. 返回
        return RaffleAwardEntity(awardId = awardId)
    }

    abstract fun doCheckRaffleBeforeFilter(raffleInputEntity: RaffleInputEntity, logics: List<String>): RuleActionEntity

}