package com.ugrong.framework.redis.repository.impl;

import com.ugrong.framework.redis.repository.ListRedisRepository;
import org.springframework.data.redis.core.BoundListOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractListRedisRepository<T> extends AbstractRedisRepository<T> implements ListRedisRepository<T> {

    @Override
    public final BoundListOperations<String, T> getListOps(String keySuffix) {
        return super.getRedisTemplate().boundListOps(super.getKey(this.getKeyPrefix(), keySuffix));
    }

    @Override
    public void set(String keySuffix, T[] values) {
        preSetProcess(values);
        this.getListOps(keySuffix).leftPushAll(values);
    }

    @Override
    public void set(String keySuffix, T value) {
        preSetProcess(value);
        this.getListOps(keySuffix).leftPush(value);
    }

    @Override
    public void setWithDefaultTimeOut(String keySuffix, T value) {
        this.setWithTimeOut(keySuffix, value, 7, TimeUnit.DAYS);
    }

    @Override
    public void setWithDefaultTimeOut(String keySuffix, T[] values) {
        this.setWithTimeOut(keySuffix, values, 7, TimeUnit.DAYS);
    }

    @Override
    public void setWithTimeOut(String keySuffix, T value, long timeout, TimeUnit timeUnit) {
        preSetProcess(value);
        BoundListOperations<String, T> operations = this.getListOps(keySuffix);
        operations.leftPush(value);
        operations.expire(timeout, timeUnit);
    }

    @Override
    public void setWithTimeOut(String keySuffix, T[] values, long timeout, TimeUnit timeUnit) {
        preSetProcess(values);
        BoundListOperations<String, T> operations = this.getListOps(keySuffix);
        operations.leftPushAll(values);
        operations.expire(timeout, timeUnit);
    }

    @Override
    public List<T> getAll(String keySuffix) {
        return this.getListOps(keySuffix).range(0, -1);
    }

    @Override
    public void preSetProcess(T[] values) {

    }
}
