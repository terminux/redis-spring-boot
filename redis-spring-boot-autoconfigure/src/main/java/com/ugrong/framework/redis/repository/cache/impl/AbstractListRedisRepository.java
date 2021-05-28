package com.ugrong.framework.redis.repository.cache.impl;

import com.ugrong.framework.redis.repository.cache.IListRedisRepository;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractListRedisRepository<T extends Serializable> extends AbstractRedisObjectRepository<T> implements IListRedisRepository<T> {

    private BoundListOperations<String, T> getListOperation(String keySuffix) {
        return super.geTemplate().boundListOps(super.getKey(keySuffix));
    }

    @Override
    public final void add(String keySuffix, T value) {
        Assert.notNull(value, "This redis value is required; it must not be null");
        this.getListOperation(keySuffix).rightPush(value);
    }

    @Override
    public void addWithDefaultTimeout(String keySuffix, T value) {
        this.addWithTimeout(keySuffix, value, 7, TimeUnit.DAYS);
    }

    @Override
    public final void addWithTimeout(String keySuffix, T value, long timeout, TimeUnit timeUnit) {
        Assert.notNull(value, "This redis value is required; it must not be null");
        this.validTimeArgs(timeout, timeUnit);
        BoundListOperations<String, T> operation = this.getListOperation(keySuffix);
        operation.rightPush(value);
        operation.expire(timeout, timeUnit);
    }

    @Override
    public final Long addAll(String keySuffix, T[] values) {
        Assert.notEmpty(values, "This redis values is required; it must not be empty");
        return this.getListOperation(keySuffix).rightPushAll(values);
    }

    @Override
    public Long addAllWithDefaultTimeout(String keySuffix, T[] values) {
        return this.addAllWithTimeout(keySuffix, values, 7, TimeUnit.DAYS);
    }

    @Override
    public final Long addAllWithTimeout(String keySuffix, T[] values, long timeout, TimeUnit timeUnit) {
        Assert.notEmpty(values, "This redis values is required; it must not be empty");
        this.validTimeArgs(timeout, timeUnit);
        BoundListOperations<String, T> operation = this.getListOperation(keySuffix);
        Long count = operation.rightPushAll(values);
        operation.expire(timeout, timeUnit);
        return count;
    }

    @Override
    public void expireWithDefaultTimeout(String keySuffix) {
        this.expire(keySuffix, 7, TimeUnit.DAYS);
    }

    @Override
    public final void expire(String keySuffix, long timeout, TimeUnit timeUnit) {
        this.getListOperation(keySuffix).expire(timeout, timeUnit);
    }

    @Override
    public List<T> get(String keySuffix) {
        return this.getListOperation(keySuffix).range(0, -1);
    }

    @Override
    public Long size(String keySuffix) {
        return this.getListOperation(keySuffix).size();
    }
}
