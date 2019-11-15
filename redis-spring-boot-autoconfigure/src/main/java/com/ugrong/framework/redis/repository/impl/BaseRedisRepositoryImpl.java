package com.ugrong.framework.redis.repository.impl;

import com.ugrong.framework.redis.domain.RedisKeyPrefix;
import com.ugrong.framework.redis.repository.BaseRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

public class BaseRedisRepositoryImpl<T> implements BaseRedisRepository<T> {

    @Autowired
    RedisTemplate<String, T> redisTemplate;

    private static final String DELIMITER = ":";

    @Override
    public ValueOperations<String, T> getValueOps(String key) {
        Assert.hasText(key, "This redis key is required; it must not be null");
        return this.redisTemplate.opsForValue();
    }

    @Override
    public String getKey(RedisKeyPrefix keyPrefix, String keySuffix) {
        Assert.hasText(keySuffix, "This redis key suffix is required; it must not be null");
        return this.joinKey(keyPrefix, keySuffix);
    }

    private String joinKey(RedisKeyPrefix keyPrefix, String keySuffix) {
        Assert.isTrue(keyPrefix != null && StringUtils.hasText(keyPrefix.getPrefix()),
                "This redis key prefix is required; it must not be null");
        String key = StringUtils.trimWhitespace(keyPrefix.getPrefix());
        if (StringUtils.hasText(keySuffix)) {
            key = key.concat(DELIMITER).concat(StringUtils.trimWhitespace(keySuffix));
        }
        return key;
    }

    private void validateKey(RedisKeyPrefix keyPrefix, String keySuffix) {
        Assert.isTrue(keyPrefix != null && StringUtils.hasText(keyPrefix.getPrefix()),
                "This redis key prefix is required; it must not be null");

        Assert.hasText(keySuffix, "This redis key suffix is required; it must not be null");
    }

    @Override
    public RedisTemplate<String, T> getRedisTemplate() {
        return this.redisTemplate;
    }

    @Override
    public final void setValue(String key, T value) {
        ValueOperations<String, T> operations = this.getValueOps(key);
        operations.set(key, value);
    }

    @Override
    public void setValue(RedisKeyPrefix keyPrefix, String keySuffix, T value) {
        this.setValue(getKey(keyPrefix, keySuffix), value);
    }

    @Override
    public final void setValueWithTimeOut(String key, T value, long timeout, TimeUnit timeUnit) {
        ValueOperations<String, T> operations = this.getValueOps(key);
        operations.set(key, value, timeout, timeUnit);
    }

    @Override
    public void setValueWithTimeOut(RedisKeyPrefix keyPrefix, String keySuffix, T value, long timeout, TimeUnit timeUnit) {
        this.setValueWithTimeOut(getKey(keyPrefix, keySuffix), value, timeout, timeUnit);
    }

    @Override
    public final void setValueWithDefaultTimeOut(String key, T value) {
        this.setValueWithTimeOut(key, value, 7, TimeUnit.DAYS);
    }

    @Override
    public void setValueWithDefaultTimeOut(RedisKeyPrefix keyPrefix, String keySuffix, T value) {
        this.setValueWithDefaultTimeOut(getKey(keyPrefix, keySuffix), value);
    }

    @Override
    public final T getValue(String key) {
        return this.getValueOps(key).get(key);
    }

    @Override
    public T getValue(RedisKeyPrefix keyPrefix, String keySuffix) {
        return this.getValue(getKey(keyPrefix, keySuffix));
    }

    @Override
    public final void removeValue(String key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public void removeValue(RedisKeyPrefix keyPrefix, String keySuffix) {
        this.removeValue(getKey(keyPrefix, keySuffix));
    }

    @Override
    public void expire(String key, long timeout, TimeUnit timeUnit) {
        this.setValueWithTimeOut(key, getValue(key), timeout, timeUnit);
    }

    @Override
    public void expire(RedisKeyPrefix keyPrefix, String keySuffix, long timeout, TimeUnit timeUnit) {
        String key = this.getKey(keyPrefix, keySuffix);
        this.setValueWithTimeOut(key, getValue(key), timeout, timeUnit);
    }

    @Override
    public Boolean hasKey(String key) {
        return StringUtils.hasText(key) ? this.redisTemplate.hasKey(key) : false;
    }

    @Override
    public Boolean hasKey(RedisKeyPrefix keyPrefix, String keySuffix) {
        return this.hasKey(this.getKey(keyPrefix, keySuffix));
    }

    @Override
    public Long getAndIncrement(String key) {
        return this.getAndIncrementBy(key, 1);
    }

    @Override
    public Long getAndIncrement(RedisKeyPrefix keyPrefix, String keySuffix) {
        return this.getAndIncrementBy(keyPrefix, keySuffix, 1);
    }

    @Override
    public Long getAndIncrementBy(String key, Integer incrementBy) {
        return getValueOps(key).increment(key, incrementBy);
    }

    @Override
    public Long getAndIncrementBy(RedisKeyPrefix keyPrefix, String keySuffix, Integer incrementBy) {
        return this.getAndIncrementBy(this.getKey(keyPrefix, keySuffix), incrementBy);
    }

    @Override
    public Long getAndDecrement(String key) {
        return this.getAndDecrementBy(key, 1);
    }

    @Override
    public Long getAndDecrement(RedisKeyPrefix keyPrefix, String keySuffix) {
        return this.getAndDecrement(this.getKey(keyPrefix, keySuffix));
    }

    @Override
    public Long getAndDecrementBy(String key, Integer decrementBy) {
        return getValueOps(key).decrement(key, decrementBy);
    }

    @Override
    public Long getAndDecrementBy(RedisKeyPrefix keyPrefix, String keySuffix, Integer decrementBy) {
        return this.getAndDecrementBy(this.getKey(keyPrefix, keySuffix), decrementBy);
    }

    @Override
    public Long getExpire(String key) {
        return this.redisTemplate.getExpire(key);
    }

    @Override
    public Long getExpire(RedisKeyPrefix keyPrefix, String keySuffix) {
        return this.getExpire(this.getKey(keyPrefix, keySuffix));
    }
}
