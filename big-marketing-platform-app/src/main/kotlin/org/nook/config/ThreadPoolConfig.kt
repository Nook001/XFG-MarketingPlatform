package org.nook.config

import lombok.extern.slf4j.Slf4j
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties::class)
class ThreadPoolConfig {
    @Bean
    @ConditionalOnMissingBean(ThreadPoolExecutor::class)
    @Throws(ClassNotFoundException::class, InstantiationException::class, IllegalAccessException::class)
    fun threadPoolExecutor(properties: ThreadPoolConfigProperties): ThreadPoolExecutor {
        // 实例化策略
        val handler = when (properties.POLICY) {
            "AbortPolicy" -> ThreadPoolExecutor.AbortPolicy()
            "DiscardPolicy" -> ThreadPoolExecutor.DiscardPolicy()
            "DiscardOldestPolicy" -> ThreadPoolExecutor.DiscardOldestPolicy()
            "CallerRunsPolicy" -> ThreadPoolExecutor.CallerRunsPolicy()
            else -> ThreadPoolExecutor.AbortPolicy()
        }
        // 创建线程池
        return ThreadPoolExecutor(
            properties.CORE_POOL_SIZE,
            properties.MAX_POOL_SIZE,
            properties.KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            LinkedBlockingQueue<Runnable?>(properties.BLOCK_QUEUE_SIZE),
            Executors.defaultThreadFactory(),
            handler
        )
    }
}
