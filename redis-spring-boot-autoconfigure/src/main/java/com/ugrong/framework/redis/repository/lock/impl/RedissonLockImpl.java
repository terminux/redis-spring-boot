package com.ugrong.framework.redis.repository.lock.impl;

import com.ugrong.framework.redis.domain.IRedisLockType;
import com.ugrong.framework.redis.repository.AbstractRedisRepository;
import com.ugrong.framework.redis.repository.lock.IRedisLockRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedissonLockImpl extends AbstractRedisRepository<IRedisLockType> implements IRedisLockRepository {

    public static final long DEFAULT_LOCK_TIMEOUT = 30;

    public static final long DEFAULT_LOCK_WAIT_TIME = 10;

    private final RedissonClient redisson;

    public RedissonLockImpl(RedissonClient redisson) {
        this.redisson = redisson;
    }

    @Override
    public String getLockKey(IRedisLockType lockType, String lockField) {
        return super.getKey(lockType, lockField);
    }

    private RLock getLock(IRedisLockType lockType, String lockField) {
        return redisson.getLock(this.getLockKey(lockType, lockField));
    }

    @Override
    public boolean lockWithDefaultTimeout(IRedisLockType lockType, String lockField) {
        return this.lockWithTimeout(lockType, lockField, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public boolean lockWithTimeout(IRedisLockType lockType, String lockField, long timeout, TimeUnit timeUnit) {
        return this.tryLock(lockType, lockField, DEFAULT_LOCK_WAIT_TIME, timeout, timeUnit);
    }

    @Override
    public boolean tryLockWithDefaultTimeout(IRedisLockType lockType, String lockField) {
        return this.tryLock(lockType, lockField, DEFAULT_LOCK_WAIT_TIME, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock(IRedisLockType lockType, String lockField, long waitTime, long timeout, TimeUnit timeUnit) {
        RLock lock = this.getLock(lockType, lockField);
        try {
            lock.tryLock(waitTime, timeout, timeUnit);
            log.debug("Lock successfully obtained.lockType=[{}],lockField=[{}]", lockType.getValue(), lockField);
            return true;
        } catch (InterruptedException e) {
            log.error("Failed to get redis lock.lockType=[{}],lockField=[{}]", lockType.getValue(), lockField, e);
        }
        return false;
    }

    @Override
    public boolean unlock(IRedisLockType lockType, String lockField) {
        RLock lock = this.getLock(lockType, lockField);
        if (lock.isHeldByCurrentThread()) {
            //检查当前线程是否获得此锁
            //保证在分布式系统环境下，加锁解锁必须由同一台服务器进行，
            //不能出现你加的锁，别人给你解锁了
            try {
                lock.unlock();
                log.debug("Lock release successful.lock.lockType=[{}],lockField=[{}]", lockType.getValue(), lockField);
            } catch (Exception e) {
                log.error("Failed to release redis lock.lockType=[{}],lockField=[{}]", lockType.getValue(), lockField, e);
                return false;
            }
        }
        return true;
    }
}
