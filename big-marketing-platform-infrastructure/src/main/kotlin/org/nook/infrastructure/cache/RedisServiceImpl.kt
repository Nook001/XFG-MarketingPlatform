package org.nook.infrastructure.cache

import jakarta.annotation.Resource
import org.redisson.api.*
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Redis 服务 - Redisson
 */
@Service
class RedisServiceImpl : RedisService {
    @Resource
    private lateinit var redissonClient: RedissonClient


    override fun <T> setValue(key: String, value: T) =
        redissonClient.getBucket<T>(key).set(value)

    override fun <T> setValue(key: String, value: T, expired: Long) =
        redissonClient.getBucket<T>(key).set(value, Duration.ofMillis(expired))

    override fun <T> getValue(key: String): T? =
        redissonClient.getBucket<T>(key).get()


    override fun <T> getQueue(key: String): RQueue<T> =
        redissonClient.getQueue(key)


    override fun <T> getBlockingQueue(key: String): RBlockingQueue<T> =
        redissonClient.getBlockingQueue(key)

    override fun <T> getDelayedQueue(rBlockingQueue: RBlockingQueue<T>): RDelayedQueue<T> =
        redissonClient.getDelayedQueue(rBlockingQueue)

    override fun setAtomicLong(key: String, value: Long) =
        redissonClient.getAtomicLong(key).set(value)

    override fun getAtomicLong(key: String): Long =
        redissonClient.getAtomicLong(key).get()

    override fun incr(key: String): Long =
        redissonClient.getAtomicLong(key).incrementAndGet()


    override fun incrBy(key: String, delta: Long): Long =
        redissonClient.getAtomicLong(key).addAndGet(delta)

    override fun decr(key: String): Long =
        redissonClient.getAtomicLong(key).decrementAndGet()

    override fun decrBy(key: String, delta: Long): Long =
        redissonClient.getAtomicLong(key).addAndGet(-delta)

    override fun remove(key: String): Boolean =
        redissonClient.getBucket<Any>(key).delete()

    override fun isExists(key: String): Boolean =
        redissonClient.getBucket<Any>(key).isExists


    override fun addToSet(key: String, value: String): Boolean =
        redissonClient.getSet<String>(key).add(value)

    override fun isSetMember(key: String, value: String): Boolean =
        redissonClient.getSet<String>(key).contains(value)

    override fun addToList(key: String, value: String): Boolean =
        redissonClient.getList<String>(key).add(value)


    override fun getFromList(key: String, index: Int): String? =
        redissonClient.getList<String>(key)[index]


    override fun <K, V> getMap(key: String): RMap<K, V> =
        redissonClient.getMap(key)

    override fun addToMap(key: String, field: String, value: String) =
        redissonClient.getMap<String, String>(key).set(field, value)


    override fun getFromMap(key: String, field: String): String? =
        redissonClient.getMap<String, String>(key)[field]


    override fun <K, V> getFromMap(key: String, field: K): V? =
        redissonClient.getMap<K, V>(key)[field]

    override fun addToSortedSet(key: String, value: String): Boolean =
        redissonClient.getSortedSet<String>(key).add(value)

    override fun getLock(key: String): RLock =
        redissonClient.getLock(key)

    override fun getFairLock(key: String): RLock =
        redissonClient.getFairLock(key)

    override fun getReadWriteLock(key: String): RReadWriteLock =
        redissonClient.getReadWriteLock(key)

    override fun getSemaphore(key: String): RSemaphore =
        redissonClient.getSemaphore(key)


    override fun getPermitExpirableSemaphore(key: String): RPermitExpirableSemaphore =
        redissonClient.getPermitExpirableSemaphore(key)


    override fun getCountDownLatch(key: String): RCountDownLatch =
        redissonClient.getCountDownLatch(key)


    override fun <T> getBloomFilter(key: String): RBloomFilter<T> =
        redissonClient.getBloomFilter(key)

    override fun setNx(key: String): Boolean =
        redissonClient.getBucket<String>(key).setIfAbsent("lock")


    override fun setNx(key: String, expired: Long, timeUnit: TimeUnit): Boolean =
        redissonClient.getBucket<String>(key).setIfAbsent("lock", Duration.of(expired, timeUnit.toChronoUnit()))
}
