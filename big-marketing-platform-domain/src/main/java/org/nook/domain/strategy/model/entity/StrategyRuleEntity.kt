package org.nook.domain.strategy.model.entity

import org.nook.types.common.Constants

data class StrategyRuleEntity(
    val strategyId: Int,
    val awardId: Int?,
    val ruleType: Int,
    val ruleModel: String,
    val ruleValue: String,
    val ruleDesc: String
) {

    fun getRuleWeightValues(): Map<String, List<Int>> {
        if (ruleModel != Constants.RuleModel.WEIGHT) return mutableMapOf()  // ← 改为可变 Map
        // 规则值格式: 4000:102,103,104;6000:201,202,203...
        // 按照分号拆分成组 4000:102,103,104 和 6000:201,202,203...
        val ruleValueGroups: List<String> = ruleValue.split(Constants.Split.SEMICOLON)

        // 解析后的Map [4000: (102,103,104)], [6000: (201,202,203)]
        val resultMap = mutableMapOf<String, MutableList<Int>>()
        for (group in ruleValueGroups) {
            val parts: List<String> = group.split(Constants.Split.COLON)
            // 每组必须包含[权重]和[奖品ID列表]两部分
            if (parts.size != 2) continue

            // 解析权重和奖品ID列表
            val weight: String = parts[0]
            val awardIds: List<Int> = parts[1].split(Constants.Split.COMMA).mapNotNull { it.toIntOrNull() }

            // 加入结果Map
            resultMap[weight] = awardIds.toMutableList()
        }
        return resultMap
    }

}
