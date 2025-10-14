package org.nook.test.infrastructure

import jakarta.annotation.Resource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.nook.infrastructure.persistent.mapper.AwardMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AwardMapperTest {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(AwardMapperTest::class.java)
    }

    @Resource
    private lateinit var awardMapper: AwardMapper

    @Test
    public fun testGet10Awards(): Unit {
        val awards = awardMapper.get10Awards()
        log.info(awards.toString())
        assertEquals(10, awards.size)
    }
}