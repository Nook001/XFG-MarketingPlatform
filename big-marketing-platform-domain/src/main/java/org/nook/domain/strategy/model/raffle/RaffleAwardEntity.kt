package org.nook.domain.strategy.model.raffle

data class RaffleAwardEntity(
    val strategyId: Long? = null,
    val awardId: Int,
    val awardKey: String? = null,
    val awardConfig: String? = null,
    val awardDesc: String? = null,
)
