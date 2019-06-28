package com.ugrong.framework.redis.lock.service;

import com.ugrong.framework.redis.domain.RedisLockType;

import java.util.concurrent.TimeUnit;

public interface RedisLockService {

    /**
     * 获取锁，返回获取状态，锁的默认超时时间为30秒
     *
     * @param lockType
     * @param field
     * @param lockValue
     * @return
     */
    boolean lockWithDefaultTimeout(RedisLockType lockType, String field, String lockValue);

    boolean lockWithDefaultTimeout(String field, String lockValue);

    /**
     * 获取锁，返回获取状态
     *
     * @param lockType
     * @param field
     * @param lockValue
     * @param timeout
     * @param timeUnit
     * @return
     */
    boolean lock(RedisLockType lockType, String field, String lockValue, long timeout, TimeUnit timeUnit);

    boolean lock(String field, String lockValue, long timeout, TimeUnit timeUnit);

    /**
     * 以时间间隔为5秒，最多重试5次和默认锁超时时间30秒的方式去获得锁
     *
     * @param lockType
     * @param field
     * @param lockValue
     * @return
     */
    boolean tryLockWithDefaultTimeout(RedisLockType lockType, String field, String lockValue);

    boolean tryLockWithDefaultTimeout(String field, String lockValue);

    /**
     * 轮询的方式去获得锁，超过轮询次数或异常返回false
     *
     * @param lockType
     * @param field
     * @param lockValue
     * @param timeout
     * @param timeUnit
     * @param tryIntervalSeconds
     * @param maxTryCount
     * @return
     */
    boolean tryLock(RedisLockType lockType, String field, String lockValue, long timeout,
                    TimeUnit timeUnit, long tryIntervalSeconds, int maxTryCount);

    boolean tryLock(String field, String lockValue, long timeout,
                    TimeUnit timeUnit, long tryIntervalSeconds, int maxTryCount);

    /**
     * 释放锁
     *
     * @param lockType
     * @param lockField
     * @param expectedValue
     * @return
     */
    boolean unlock(RedisLockType lockType, String lockField, String expectedValue);

    boolean unlock(String lockField, String expectedValue);
}
