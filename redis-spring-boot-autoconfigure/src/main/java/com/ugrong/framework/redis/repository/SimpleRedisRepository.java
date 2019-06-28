package com.ugrong.framework.redis.repository;

import com.ugrong.framework.redis.domain.RedisKeyPrefix;

import java.util.concurrent.TimeUnit;

public interface SimpleRedisRepository<T> extends BaseRedisRepository<T> {

    RedisKeyPrefix getKeyPrefix();

    void set(String keySuffix, T value);

    T get(String keySuffix);

    void setWithDefaultTimeOut(String keySuffix, T value);

    void setWithTimeOut(String keySuffix, T value, long timeout, TimeUnit timeUnit);

    void remove(String keySuffix);

    void expire(String keySuffix, long timeout, TimeUnit timeUnit);

    void preSetProcess(T value);

    void postGetProcess(T value);
}
