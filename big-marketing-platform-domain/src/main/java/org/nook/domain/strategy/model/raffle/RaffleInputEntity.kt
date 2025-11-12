package org.nook.domain.strategy.model.raffle


/**
 * 抽奖过滤规则 输入对象
 */
data class RaffleInputEntity(
    val userId: String,
    val strategyId: Long,
)
