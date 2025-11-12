package org.nook.domain.strategy.service.raffle

import org.nook.domain.strategy.model.raffle.RaffleAwardEntity
import org.nook.domain.strategy.model.raffle.RaffleInputEntity

/**
 * 抽奖策略接口
 */
interface IRaffleStrategy {
    fun performRaffle(raffleInputEntity: RaffleInputEntity): RaffleAwardEntity
}