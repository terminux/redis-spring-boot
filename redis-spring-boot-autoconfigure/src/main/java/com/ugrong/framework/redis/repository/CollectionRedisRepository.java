package com.ugrong.framework.redis.repository;

import com.ugrong.framework.redis.domain.RedisKeyPrefix;
import org.springframework.data.redis.core.BoundHashOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface CollectionRedisRepository<T> extends BaseRedisRepository<T> {

    BoundHashOperations<String, String, T> getHashOps(String keySuffix);

    RedisKeyPrefix getKeyPrefix();

    void set(String keySuffix, String field, T value);

    void setWithDefaultTimeout(String keySuffix, String field, T value);

    void setWithTimeOut(String keySuffix, String field, T value, long timeOut, TimeUnit timeUnit);

    void remove(String keySuffix, String field);

    T get(String keySuffix, String field);

    List<T> get(String keySuffix);

    void preSetProcess(T value);

    void postGetProcess(T value);
}
