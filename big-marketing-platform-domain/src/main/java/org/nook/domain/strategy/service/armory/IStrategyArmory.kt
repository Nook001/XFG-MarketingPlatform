package org.nook.domain.strategy.service.armory


/**
 * 负责初始化抽奖策略
 */

interface IStrategyArmory {

    /**
     * 初始化抽奖策略(缓存奖品概率槽)
     * @param strategyId 抽奖策略ID
     */
    fun assembleStrategy(strategyId: Long )


}