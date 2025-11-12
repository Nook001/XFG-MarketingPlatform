package org.nook.domain.strategy.service.dispatcher

interface IStrategyDispatcher {
    /**
     * 随机抽取
     */
    fun getRandomAwardId(strategyId: Long): Int

    fun getRandomAwardId(strategyId: Long, ruleWeightValue: String): Int

}