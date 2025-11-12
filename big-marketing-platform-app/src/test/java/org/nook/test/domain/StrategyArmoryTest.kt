package org.nook.test.domain

import jakarta.annotation.Resource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.nook.Application
import org.nook.domain.strategy.service.dispatcher.IStrategyDispatcher
import org.nook.domain.strategy.service.armory.StrategyArmory
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [Application::class])
class StrategyArmoryTest{
    @Resource
    private lateinit var strategyArmory: StrategyArmory

    @Resource
    private lateinit var strategyDispatcher: IStrategyDispatcher

    @BeforeEach
    fun test_armory() {
        strategyArmory.assembleStrategy(1)
    }

    @Test
    fun test_armory_random() {
        for (i in 1..10) {
            val awardId = strategyDispatcher.getRandomAwardId(1)
            println("随机奖品ID: $awardId")
        }
    }

    @Test
    fun test_armory_random_rule_weight() {
        for (i in 1..10) {
            println("测试4000配置策略 ${strategyDispatcher.getRandomAwardId(1,"4000")}")
            println("测试6000配置策略 ${strategyDispatcher.getRandomAwardId(1,"5000")}")
            println("测试10000配置策略 ${strategyDispatcher.getRandomAwardId(1,"6000")}")
        }
    }



}