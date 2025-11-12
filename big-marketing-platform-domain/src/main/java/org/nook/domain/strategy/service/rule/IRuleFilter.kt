package org.nook.domain.strategy.service.rule

import org.nook.domain.strategy.model.raffle.RaffleResultEntity
import org.nook.domain.strategy.model.raffle.RuleActionEntity
import org.nook.domain.strategy.model.raffle.RaffleFilterEntity

/**
 * 抽奖规则过滤
 */
interface IRuleFilter<out T : RaffleResultEntity> {
    /**
     * 根据上下文决定要不要过滤
     */
    fun filter(raffleFilterEntity: RaffleFilterEntity): RuleActionEntity
}