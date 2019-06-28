package com.ugrong.framework.redis.lock.service.impl;

import com.ugrong.framework.redis.domain.RedisLockType;
import com.ugrong.framework.redis.lock.service.RedisLockService;
import com.ugrong.framework.redis.model.RedisDefaultLockType;
import com.ugrong.framework.redis.model.RedisLockKeyPrefix;
import com.ugrong.framework.redis.repository.impl.LockRedisRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class RedisLockServiceImpl implements RedisLockService {

    @Autowired
    private LockRedisRepositoryImpl lockRedisRepository;

    @Override
    public boolean lockWithDefaultTimeout(RedisLockType lockType, String field, String lockValue) {
        return this.lock(lockType, field, lockValue, 30, TimeUnit.SECONDS);
    }

    @Override
    public boolean lockWithDefaultTimeout(String field, String lockValue) {
        return this.lockWithDefaultTimeout(RedisDefaultLockType.DEFAULT_TYPE, field, lockValue);
    }

    @Override
    public boolean lock(RedisLockType lockType, String field, String lockValue, long timeout, TimeUnit timeUnit) {
        return lockRedisRepository.lock(RedisLockKeyPrefix.LOCK, lockType, field, lockValue, timeout, timeUnit);
    }

    @Override
    public boolean lock(String field, String lockValue, long timeout, TimeUnit timeUnit) {
        return this.lock(RedisDefaultLockType.DEFAULT_TYPE, field, lockValue, timeout, timeUnit);
    }

    @Override
    public boolean tryLockWithDefaultTimeout(RedisLockType lockType, String field, String lockValue) {
        return this.tryLock(lockType, field, lockValue, 30, TimeUnit.SECONDS, 5l, 5);
    }

    @Override
    public boolean tryLockWithDefaultTimeout(String field, String lockValue) {
        return this.tryLockWithDefaultTimeout(RedisDefaultLockType.DEFAULT_TYPE, field, lockValue);

    }

    @Override
    public boolean tryLock(RedisLockType lockType, String field, String lockValue, long timeout, TimeUnit timeUnit, long tryIntervalSeconds, int maxTryCount) {
        return lockRedisRepository.tryLock(RedisLockKeyPrefix.LOCK, lockType, field, lockValue, timeout, timeUnit, tryIntervalSeconds, maxTryCount);
    }

    @Override
    public boolean tryLock(String field, String lockValue, long timeout, TimeUnit timeUnit, long tryIntervalSeconds, int maxTryCount) {
        return this.tryLock(RedisDefaultLockType.DEFAULT_TYPE, field, lockValue, timeout, timeUnit, tryIntervalSeconds, maxTryCount);
    }

    @Override
    public boolean unlock(RedisLockType lockType, String lockField, String expectedValue) {
        return lockRedisRepository.unlock(RedisLockKeyPrefix.LOCK, lockType, lockField, expectedValue);
    }

    @Override
    public boolean unlock(String lockField, String expectedValue) {
        return this.unlock(RedisDefaultLockType.DEFAULT_TYPE, lockField, expectedValue);
    }
}
