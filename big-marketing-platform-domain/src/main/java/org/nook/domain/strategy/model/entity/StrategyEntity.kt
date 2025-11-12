package org.nook.domain.strategy.model.entity

import org.nook.types.common.Constants

data class StrategyEntity(
    val strategyId: Long,
    val strategyDesc: String,
    val ruleModels: String?
) {
    fun getRuleModelList(): List<String> {
        return ruleModels?.split(Constants.Split.COMMA) ?: mutableListOf()  // ← 改为可变 List
    }
}
