package com.ugrong.framework.redis.repository.impl;

import com.ugrong.framework.redis.repository.SimpleRedisRepository;

import java.util.concurrent.TimeUnit;

public abstract class AbstractRedisRepository<T> extends BaseRedisRepositoryImpl<T> implements SimpleRedisRepository<T> {

    @Override
    public void set(String keySuffix, T value) {
        super.setValue(this.getKeyPrefix(), keySuffix, value);
    }

    @Override
    public T get(String keySuffix) {
        T value = super.getValue(this.getKeyPrefix(), keySuffix);
        this.postGetProcess(value);
        return value;
    }

    @Override
    public void setWithDefaultTimeOut(String keySuffix, T value) {
        this.preSetProcess(value);
        super.setValueWithDefaultTimeOut(this.getKeyPrefix(), keySuffix, value);
    }

    @Override
    public void setWithTimeOut(String keySuffix, T value, long timeout, TimeUnit timeUnit) {
        preSetProcess(value);
        super.setValueWithTimeOut(this.getKeyPrefix(), keySuffix, value, timeout, timeUnit);
    }

    @Override
    public void remove(String keySuffix) {
        super.removeValue(getKeyPrefix(), keySuffix);
    }

    @Override
    public void expire(String keySuffix, long timeout, TimeUnit timeUnit) {
        super.expire(this.getKeyPrefix(), keySuffix, timeout, timeUnit);
    }

    @Override
    public Long getAndIncrement(String keySuffix) {
        return super.getAndIncrement(this.getKeyPrefix(), keySuffix);
    }

    @Override
    public Long getAndIncrementBy(String keySuffix, Integer incrementBy) {
        return super.getAndIncrementBy(this.getKeyPrefix(), keySuffix, incrementBy);
    }

    @Override
    public void preSetProcess(T value) {

    }

    @Override
    public void postGetProcess(T value) {

    }
}
