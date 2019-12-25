package com.ugrong.framework.redis.repository.impl;

import com.ugrong.framework.redis.domain.RedisKeyPrefix;
import com.ugrong.framework.redis.domain.RedisLockType;
import com.ugrong.framework.redis.lock.RedisLockScript;
import com.ugrong.framework.redis.repository.LockRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.data.redis.connection.lettuce.LettuceConverters.toBytes;

@Slf4j
public class LockRedisRepositoryImpl extends StringRedisRepositoryImpl implements LockRedisRepository {

    @Autowired
    private RedisLockScript redisLockScript;

    @Override
    public BoundValueOperations<String, String> getBoundValueOps(String lockKey) {
        return super.redisTemplate.boundValueOps(lockKey);
    }

    @Override
    public boolean lock(RedisKeyPrefix keyPrefix, RedisLockType lockType, String lockField, String value, long timeout, TimeUnit timeUnit) {
        String lockKey = commonValidate(keyPrefix, value, lockType, lockField);
        AtomicBoolean locked = new AtomicBoolean(Boolean.FALSE);
        try {
            //设置值并且给定超时时间，防止死锁
            Boolean hasLock = this.getBoundValueOps(lockKey).setIfAbsent(value, timeout, timeUnit);
            if (hasLock != null && hasLock) {
                log.debug("Lock successfully obtained.");
                locked.set(Boolean.TRUE);
                //开启守护线程 业务未处理完时自动给锁续时
                startDaemonThread(keyPrefix, lockType, lockField, value, timeout, timeUnit);
            }
        } catch (Exception e) {
            log.error("Failed to get redis lock.{}", e);
        }
        return locked.get();
    }

    private void startDaemonThread(RedisKeyPrefix keyPrefix, RedisLockType lockType, String lockField, String value, long timeout, TimeUnit timeUnit) {
        long timeoutSeconds = getTimeoutSeconds(timeout, timeUnit);
        long waitTimeSeconds = Math.round((double) timeoutSeconds * 2 / 3);

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleWithFixedDelay(() -> {
            try {
                if (this.expandLock(keyPrefix, lockType, lockField, value, timeoutSeconds)) {
                    log.debug("The expiration time of the lock was successfully flushed." +
                                    "waitTimeSeconds=[{}s],keyPrefix=[{}],lockType=[{}],lockField=[{}],lockValue=[{}],timeoutSeconds=[{}s]",
                            waitTimeSeconds, keyPrefix, lockType, lockField, value, timeoutSeconds);
                } else {
                    //锁已过期
                    if (!executor.isShutdown()) {
                        executor.shutdown();
                    }
                }
            } catch (Exception e) {
                log.error("Failed to refresh the expiration time of the lock.{}", e);
                if (!executor.isShutdown()) {
                    executor.shutdown();
                }
            }
        }, waitTimeSeconds, waitTimeSeconds, TimeUnit.SECONDS);
    }

    private long getTimeoutSeconds(long timeout, TimeUnit timeUnit) {
        if (TimeUnit.SECONDS == timeUnit) {
            return timeout;
        }
        return timeUnit.toSeconds(timeout);
    }

    @Override
    public boolean tryLock(final RedisKeyPrefix keyPrefix, final RedisLockType lockType, final String lockField, final String value,
                           final long timeout, final TimeUnit timeUnit, final long tryIntervalSeconds, final int maxTryCount) {

        AtomicInteger tryCount = new AtomicInteger();
        while (true) {
            final int currentCount = tryCount.incrementAndGet();
            if (currentCount > maxTryCount) {
                log.debug("Get lock timeout");
                return false;
            }
            log.debug("The number of times a lock is currently acquired:{}", currentCount);
            if (this.lock(keyPrefix, lockType, lockField, value, timeout, timeUnit)) {
                return true;
            }
            try {
                TimeUnit.SECONDS.sleep(tryIntervalSeconds);
            } catch (InterruptedException e) {
                log.error("The lock acquisition was interrupted.{}", e);
                return false;
            }
        }
    }

    @Override
    public boolean unlock(RedisKeyPrefix keyPrefix, RedisLockType lockType, String lockField, String expectedValue) {
        String lockKey = commonValidate(keyPrefix, expectedValue, lockType, lockField);

//        String actualValue = super.getValue(lockKey);
//        if (expectedValue.equals(actualValue)) {
//            super.removeValue(lockKey);
//            return true;
//        }

        /**
         * 由于判断和delete()操作都不是原子性的，
         * 所以使用lua脚本来代替注释代码，
         * 保证在分布式系统环境下，加锁解锁必须由同一台服务器进行，
         * 不能出现你加的锁，别人给你解锁了。
         */
        boolean unlock = execLuaScript(redisLockScript.getUnlockLuaScript(), toBytes(lockKey), toBytes(expectedValue));
        if (unlock) {
            log.debug("Lock release successful.lockKey=[{}],expectedValue=[{}]", lockKey, expectedValue);
        } else {
            log.error("Failed to release redis lock.lockKey=[{}],expectedValue=[{}]", lockKey, expectedValue);
        }
        return unlock;
    }

    @Override
    public boolean expandLock(RedisKeyPrefix keyPrefix, RedisLockType lockType, String lockField, String expectedValue, long timeoutSeconds) {
        String lockKey = commonValidate(keyPrefix, expectedValue, lockType, lockField);
        return execLuaScript(redisLockScript.getExpandLockLuaScript(), toBytes(lockKey), toBytes(expectedValue), toBytes(timeoutSeconds));
    }

    private boolean execLuaScript(String script, byte[]... params) {
        Boolean result = super.redisTemplate.execute((RedisCallback<Boolean>) action -> action.eval(
                toBytes(script),
                ReturnType.BOOLEAN,
                1,
                params
        ));
        return result == null ? false : result;
    }

    private String commonValidate(RedisKeyPrefix keyPrefix, String value, RedisLockType lockType, String lockField) {
        Assert.isTrue(lockType != null && StringUtils.hasText(lockType.getLockKey()),
                "This redis lock type is required; it must not be null.");
        Assert.hasText(lockField, "This redis lock field is required; it must not be null.");
        Assert.hasText(value, "This redis lock value is required; it must not be null.");
        return super.getKey(keyPrefix, lockType.getLockKey().concat(lockField));
    }
}
