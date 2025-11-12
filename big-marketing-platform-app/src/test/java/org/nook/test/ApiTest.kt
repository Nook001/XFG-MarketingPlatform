package org.nook.test

import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.nook.Application
import org.nook.infrastructure.cache.RedisService
import org.nook.test.infrastructure.AwardMapperTest
import org.redisson.api.RMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [Application::class])
class ApiTest {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(AwardMapperTest::class.java)
    }

    @Resource
    private lateinit var redisService: RedisService

    @Test
    fun test() {
        val map: RMap<Any, Any> = redisService.getMap("strategy_id_1")
        map["1"] = "101"
        map["2"] = "101"
        map["3"] = "101"
        map["4"] = "102"
        map["5"] = "102"
        map["6"] = "102"
        map["7"] = "103"
        map["8"] = "103"
        map["9"] = "104"
        map["10"] = "115"
        
        log.info("测试结果:获取key{} 结果{}", "1", redisService.getFromMap<Any, Any>("strategy_id_1", "1"))

        log.info("测试结果:获取key{} 结果{}", "10", redisService.getFromMap<Any, Any>("strategy_id_1", "10"))
    }
}
