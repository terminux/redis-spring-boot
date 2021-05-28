package com.ugrong.framework.redis.repository.cache.impl;

import com.google.common.collect.Lists;
import com.ugrong.framework.redis.repository.cache.ISimpleRedisRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSimpleRedisRepository<T extends Serializable> extends AbstractRedisObjectRepository<T> implements ISimpleRedisRepository<T> {

    private ValueOperations<String, T> getOperation() {
        return this.geTemplate().opsForValue();
    }

    @Override
    public final void set(String keySuffix, T value) {
        Assert.notNull(value, "This redis value is required; it must not be null");
        this.getOperation().set(this.getKey(keySuffix), value);
    }

    @Override
    public final Optional<T> get(String keySuffix) {
        return Optional.ofNullable(this.getOperation().get(this.getKey(keySuffix)));
    }

    @Override
    public List<T> getAll() {
        Set<String> keys = this.keys();
        if (CollectionUtils.isNotEmpty(keys)) {
            return this.getOperation().multiGet(keys);
        }
        return Lists.newArrayList();
    }

    @Override
    public void setWithDefaultTimeout(String keySuffix, T value) {
        this.setWithTimeout(keySuffix, value, 7, TimeUnit.DAYS);
    }

    @Override
    public void setWithTimeout(String keySuffix, T value, long timeout, TimeUnit timeUnit) {
        Assert.notNull(value, "This redis value is required; it must not be null");
        this.validTimeArgs(timeout, timeUnit);
        this.getOperation().set(this.getKey(keySuffix), value, timeout, timeUnit);
    }

    @Override
    public void expireWithDefaultTimeout(String keySuffix) {
        this.expire(keySuffix, 7, TimeUnit.DAYS);
    }

    @Override
    public final void expire(String keySuffix, long timeout, TimeUnit timeUnit) {
        super.expire(this.getCacheType(), keySuffix, timeout, timeUnit);
    }
}
