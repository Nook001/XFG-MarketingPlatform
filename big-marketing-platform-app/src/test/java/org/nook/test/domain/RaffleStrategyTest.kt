package org.nook.test.domain

import com.alibaba.fastjson2.JSON
import jakarta.annotation.Resource
import org.junit.jupiter.api.BeforeEach
import org.nook.Application
import org.nook.domain.strategy.service.raffle.IRaffleStrategy
import org.junit.jupiter.api.Test
import org.nook.domain.strategy.model.raffle.RaffleInputEntity
import org.nook.domain.strategy.service.rule.filter.RuleWeightFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.ReflectionTestUtils

@SpringBootTest(classes = [Application::class])
class RaffleStrategyTest {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(RaffleStrategyTest::class.java)
    }

    @Resource
    private lateinit var raffleStrategy: IRaffleStrategy

    @Resource
    private lateinit var ruleWeightFilter: RuleWeightFilter

    @BeforeEach
    fun setUp() {
        ReflectionTestUtils.setField(ruleWeightFilter, "userPoints", 4500L)
    }

    @Test
    fun testPerformRaffle() {
        val raffleInputEntity = RaffleInputEntity(
            userId = "xiaofuge",
            strategyId = 1L
        )

        val raffleAward = raffleStrategy.performRaffle(raffleInputEntity)

        log.info("请求参数: ${JSON.toJSONString(raffleInputEntity)}")
        log.info("测试结果：: ${JSON.toJSONString(raffleAward)}")
    }

    @Test
    fun testPerformRaffleBlackList() {
        val raffleInputEntity = RaffleInputEntity(
            userId = "user003",
            strategyId = 1L
        )
        log.info("用户积分: ${ReflectionTestUtils.getField(ruleWeightFilter, "userPoints")}")

        val raffleAward = raffleStrategy.performRaffle(raffleInputEntity)

        log.info("请求参数: ${JSON.toJSONString(raffleInputEntity)}")
        log.info("测试结果: ${JSON.toJSONString(raffleAward)}")
    }

}