package com.ugrong.framework.redis.repository;

import com.ugrong.framework.redis.domain.RedisKeyPrefix;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

public interface BaseRedisRepository<T> {

    ValueOperations<String, T> getValueOps(String key);

    String getKey(RedisKeyPrefix keyPrefix, String keySuffix);

    RedisTemplate<String, T> getRedisTemplate();

    void setValue(String key, T value);

    void setValue(RedisKeyPrefix keyPrefix, String keySuffix, T value);

    void setValueWithTimeOut(String key, T value, long timeout, TimeUnit timeUnit);

    void setValueWithTimeOut(RedisKeyPrefix keyPrefix, String keySuffix, T value, long timeout, TimeUnit timeUnit);

    void setValueWithDefaultTimeOut(String key, T value);

    void setValueWithDefaultTimeOut(RedisKeyPrefix keyPrefix, String keySuffix, T value);

    T getValue(String key);

    T getValue(RedisKeyPrefix keyPrefix, String keySuffix);

    void removeValue(String key);

    void removeValue(RedisKeyPrefix keyPrefix, String keySuffix);

    void expire(String key, long timeout, TimeUnit timeUnit);

    void expire(RedisKeyPrefix keyPrefix, String keySuffix, long timeout, TimeUnit timeUnit);

    Boolean hasKey(String key);

    Boolean hasKey(RedisKeyPrefix keyPrefix, String keySuffix);

    Long getAndIncrement(String key);

    Long getAndIncrement(RedisKeyPrefix keyPrefix, String keySuffix);

    Long getAndIncrementBy(String key, Integer incrementBy);

    Long getAndIncrementBy(RedisKeyPrefix keyPrefix, String keySuffix, Integer incrementBy);
}
