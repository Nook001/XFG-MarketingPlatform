package org.nook.infrastructure.cache

import org.redisson.api.*
import java.util.concurrent.TimeUnit

/**
 * Redis 服务
 */
interface RedisService {
    /**
     * 设置指定 key 的值
     * 
     * @param key   键
     * @param value 值
     */
    fun <T> setValue(key: String, value: T)

    /**
     * 设置指定 key 的值
     * 
     * @param key     键
     * @param value   值
     * @param expired 过期时间
     */
    fun <T> setValue(key: String, value: T, expired: Long)

    /**
     * 获取指定 key 的值
     * 
     * @param key 键
     * @return 值（可能为 null，当 key 不存在时）
     */
    fun <T> getValue(key: String): T?

    /**
     * 获取队列
     * 
     * @param key 键
     * @param <T> 泛型
     * @return 队列
    </T> */
    fun <T> getQueue(key: String): RQueue<T>

    /**
     * 加锁队列
     * 
     * @param key 键
     * @param <T> 泛型
     * @return 队列
    </T> */
    fun <T> getBlockingQueue(key: String): RBlockingQueue<T>

    /**
     * 延迟队列
     * 
     * @param rBlockingQueue 加锁队列
     * @param <T>            泛型
     * @return 队列
    </T> */
    fun <T> getDelayedQueue(rBlockingQueue: RBlockingQueue<T>): RDelayedQueue<T>

    /**
     * 设置值
     * 
     * @param key   key 键
     * @param value 值
     */
    fun setAtomicLong(key: String, value: Long)

    /**
     * 获取值
     * 
     * @param key key 键
     */
    fun getAtomicLong(key: String): Long

    /**
     * 自增 Key 的值；1、2、3、4
     * 
     * @param key 键
     * @return 自增后的值
     */
    fun incr(key: String): Long

    /**
     * 指定值,自增 Key 的值；1、2、3、4
     * 
     * @param key 键
     * @return 自增后的值
     */
    fun incrBy(key: String, delta: Long): Long

    /**
     * 自减 Key 的值；1、2、3、4
     * 
     * @param key 键
     * @return 自增后的值
     */
    fun decr(key: String): Long

    /**
     * 指定值,自增 Key 的值；1、2、3、4
     * 
     * @param key 键
     * @return 自增后的值
     */
    fun decrBy(key: String, delta: Long): Long


    /**
     * 移除指定 key 的值
     * 
     * @param key 键
     */
    fun remove(key: String): Boolean

    /**
     * 判断指定 key 的值是否存在
     * 
     * @param key 键
     * @return true/false
     */
    fun isExists(key: String): Boolean

    /**
     * 将指定的值添加到集合中
     * 
     * @param key   键
     * @param value 值
     */
    fun addToSet(key: String, value: String): Boolean

    /**
     * 判断指定的值是否是集合的成员
     * 
     * @param key   键
     * @param value 值
     * @return 如果是集合的成员返回 true,否则返回 false
     */
    fun isSetMember(key: String, value: String): Boolean

    /**
     * 将指定的值添加到列表中
     * 
     * @param key   键
     * @param value 值
     */
    fun addToList(key: String, value: String): Boolean

    /**
     * 获取列表中指定索引的值
     * 
     * @param key   键
     * @param index 索引
     * @return 值
     */
    fun getFromList(key: String, index: Int): String?

    /**
     * 获取Map
     * 
     * @param key 键
     * @return 值
     */
    fun <K, V> getMap(key: String): RMap<K, V>

    /**
     * 将指定的键值对添加到哈希表中
     * 
     * @param key   键
     * @param field 字段
     * @param value 值
     */
    fun addToMap(key: String, field: String, value: String)

    /**
     * 获取哈希表中指定字段的值
     * 
     * @param key   键
     * @param field 字段
     * @return 值
     */
    fun getFromMap(key: String, field: String): String?

    /**
     * 获取哈希表中指定字段的值
     * 
     * @param key   键
     * @param field 字段
     * @return 值
     */
    fun <K, V> getFromMap(key: String, field: K): V?

    /**
     * 将指定的值添加到有序集合中
     * 
     * @param key   键
     * @param value 值
     */
    fun addToSortedSet(key: String, value: String): Boolean

    /**
     * 获取 Redis 锁（可重入锁）
     * 
     * @param key 键
     * @return Lock
     */
    fun getLock(key: String): RLock

    /**
     * 获取 Redis 锁（公平锁）
     * 
     * @param key 键
     * @return Lock
     */
    fun getFairLock(key: String): RLock

    /**
     * 获取 Redis 锁（读写锁）
     * 
     * @param key 键
     * @return RReadWriteLock
     */
    fun getReadWriteLock(key: String): RReadWriteLock

    /**
     * 获取 Redis 信号量
     * 
     * @param key 键
     * @return RSemaphore
     */
    fun getSemaphore(key: String): RSemaphore

    /**
     * 获取 Redis 过期信号量
     * 
     * 
     * 基于Redis的Redisson的分布式信号量（Semaphore）Java对象RSemaphore采用了与java.util.concurrent.Semaphore相似的接口和用法。
     * 同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
     * 
     * @param key 键
     * @return RPermitExpirableSemaphore
     */
    fun getPermitExpirableSemaphore(key: String): RPermitExpirableSemaphore

    /**
     * 闭锁
     * 
     * @param key 键
     * @return RCountDownLatch
     */
    fun getCountDownLatch(key: String): RCountDownLatch

    /**
     * 布隆过滤器
     * 
     * @param key 键
     * @param <T> 存放对象
     * @return 返回结果
    </T> */
    fun <T> getBloomFilter(key: String): RBloomFilter<T>

    fun setNx(key: String): Boolean

    fun setNx(key: String, expired: Long, timeUnit: TimeUnit): Boolean
}
