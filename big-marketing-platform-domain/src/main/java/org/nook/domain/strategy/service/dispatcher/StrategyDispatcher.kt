package org.nook.domain.strategy.service.dispatcher

import org.nook.domain.strategy.repository.IRepository
import org.springframework.stereotype.Service

@Service
class StrategyDispatcher(
    private val repository: IRepository
): IStrategyDispatcher {

    override fun getRandomAwardId(strategyId: Long): Int {
        val rateRange:Int = repository.getRateRange(strategyId)
        return repository.getAwardIdFromStrategyRateTable(strategyId, (1..rateRange).random())
    }

    override fun getRandomAwardId(strategyId: Long, ruleWeightValue: String): Int {
        val key = "$strategyId:$ruleWeightValue"
        val rateRange:Int = repository.getRateRange(key)
        return repository.getAwardIdFromStrategyRateTable(key, (1..rateRange).random())
    }
}