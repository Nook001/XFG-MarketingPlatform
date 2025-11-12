package org.nook.domain.strategy.model.raffle
/**
 *
 */
data class RaffleFilterEntity(
    val userId: String,
    val strategyId: Long,
    val awardId: Int,
    val ruleModel: String,
)
