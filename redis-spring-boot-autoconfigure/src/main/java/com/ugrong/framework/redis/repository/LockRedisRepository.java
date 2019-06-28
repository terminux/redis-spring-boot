package com.ugrong.framework.redis.repository;

import com.ugrong.framework.redis.domain.RedisKeyPrefix;
import com.ugrong.framework.redis.domain.RedisLockType;
import org.springframework.data.redis.core.BoundValueOperations;

import java.util.concurrent.TimeUnit;

public interface LockRedisRepository extends BaseRedisRepository<String> {

    BoundValueOperations<String, String> getBoundValueOps(String key);

    /**
     * 尝试获取锁，获取失败则返回false
     *
     * @param keyPrefix
     * @param lockType
     * @param lockField
     * @param value     value的值可以设置成该机器的唯一标识，例如时间+请求号或者uuid
     * @param timeout   加锁的时间，超过这个时间后锁会自动释放
     * @param timeUnit  加锁的时间单位
     * @return
     */
    boolean lock(RedisKeyPrefix keyPrefix, RedisLockType lockType, String lockField,
                 String value, long timeout, TimeUnit timeUnit);

    /**
     * 轮询的方式去获得锁，超过轮询次数或异常返回false
     *
     * @param keyPrefix
     * @param lockType
     * @param lockField
     * @param value
     * @param timeout
     * @param timeUnit
     * @param tryIntervalSeconds 重试的时间间隔(秒)
     * @param maxTryCount        最大的重试次数
     */
    boolean tryLock(RedisKeyPrefix keyPrefix, RedisLockType lockType, String lockField, String value,
                    long timeout, TimeUnit timeUnit, long tryIntervalSeconds, int maxTryCount);

    /**
     * 释放锁
     *
     * @param keyPrefix
     * @param lockType
     * @param lockField
     * @param expectedValue
     * @return
     */
    boolean unlock(RedisKeyPrefix keyPrefix, RedisLockType lockType, String lockField, String expectedValue);

    /**
     * 对锁的过期时间进行加时
     *
     * @param keyPrefix
     * @param lockType
     * @param lockField
     * @param expectedValue
     * @param timeoutSeconds
     * @return
     */
    boolean expandLock(RedisKeyPrefix keyPrefix, RedisLockType lockType, String lockField,
                       String expectedValue, long timeoutSeconds);
}
