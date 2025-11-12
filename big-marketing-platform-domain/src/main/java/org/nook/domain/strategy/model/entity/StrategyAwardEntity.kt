package org.nook.domain.strategy.model.entity

import java.math.BigDecimal

data class StrategyAwardEntity(
    val strategyId: Long = 0,
    val awardId: Int = 0,
    val awardCount: Int = 0,
    val awardCountSurplus: Int = 0,
    val awardRate: BigDecimal = BigDecimal.ZERO
)
