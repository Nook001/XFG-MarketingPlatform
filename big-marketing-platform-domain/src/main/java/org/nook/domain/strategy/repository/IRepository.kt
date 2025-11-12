package org.nook.domain.strategy.repository

import org.nook.domain.strategy.model.entity.StrategyAwardEntity
import org.nook.domain.strategy.model.entity.StrategyEntity
import org.nook.domain.strategy.model.entity.StrategyRuleEntity
import java.math.BigDecimal


interface IRepository {
    /**
     * 根据Strategy ID获取Strategy Award Entity
     */
    fun getStrategyAwardEntity(strategyId: Long): List<StrategyAwardEntity>

    /**
     * 根据Strategy ID获取Strategy Entity
     */
    fun getStrategyEntity(strategyId: Long): StrategyEntity

    /**
     * 根据Strategy ID和Award ID获取 策略内，指定奖品的规则
     */
    fun getStrategyRuleEntity(strategyId: Long, awardId: Int): StrategyRuleEntity

    /**
     * 根据Strategy ID, Award ID和Rule Model获取 策略内，指定奖品的制度规则
     */
    fun getStrategyRuleEntity(strategyId: Long, awardId: Int, ruleModel: String): StrategyRuleEntity

    /**
     * 根据Strategy ID和Rule Model获取 策略全局规则
     */
    fun getType1StrategyRuleEntity(strategyId: Long, ruleModel: String): StrategyRuleEntity







    /**
     * 储存概率表到Redis缓存中
     */
    fun storeProbabilityTable(key: String, rateRange: BigDecimal, strategyAwardSearchTable: List<Int>)

    fun getRateRange(strategyId: Long): Int

    fun getRateRange(key: String): Int

    /**
     * 从概率表中获取一个奖品ID
     */
    fun getAwardIdFromStrategyRateTable(strategyId: Long, random: Int): Int

    fun getAwardIdFromStrategyRateTable(key: String, random: Int): Int
}