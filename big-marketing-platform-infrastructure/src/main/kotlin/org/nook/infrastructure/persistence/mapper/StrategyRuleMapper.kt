package org.nook.infrastructure.persistence.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.nook.infrastructure.persistence.po.StrategyRulePO

@Mapper
interface StrategyRuleMapper {
    
    /**
     * 查询策略级规则（award_id = NULL, rule_type = 1）
     * 
     * 策略级规则特点：
     * - 每个策略的每种规则类型只有1条记录
     * - award_id 为 NULL
     * - 例如：rule_weight（权重规则）、rule_blacklist（黑名单规则）
     * 
     * @param strategyId 策略ID
     * @param ruleModel 规则模型
     * @return 策略级规则（单条记录），如果不存在返回 null
     */
    fun getStrategyRuleByStrategyIdAndRuleModel(
        @Param("strategyId") strategyId: Long,
        @Param("ruleModel") ruleModel: String
    ): StrategyRulePO?
    
    /**
     * 查询奖品级规则列表（award_id != NULL, rule_type = 2）
     * 
     * 奖品级规则特点：
     * - 每个策略的每种规则类型可能有多条记录（针对不同奖品）
     * - award_id 不为 NULL
     * - 例如：rule_lock（解锁规则）、rule_luck_award（兜底规则）
     * 
     * @param strategyId 策略ID
     * @param ruleModel 规则模型
     * @return 奖品级规则列表
     */
    fun getStrategyRuleListByStrategyIdAndRuleModel(
        @Param("strategyId") strategyId: Long,
        @Param("ruleModel") ruleModel: String
    ): List<StrategyRulePO>
    
    /**
     * 查询特定奖品的规则配置
     * 
     * @param strategyId 策略ID
     * @param awardId 奖品ID
     * @param ruleModel 规则模型
     * @return 该奖品的规则配置，如果不存在返回 null
     */
    fun getStrategyRuleByStrategyIdAndAwardIdAndRuleModel(
        @Param("strategyId") strategyId: Long,
        @Param("awardId") awardId: Int,
        @Param("ruleModel") ruleModel: String
    ): StrategyRulePO?
}