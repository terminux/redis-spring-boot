package com.ugrong.framework.redis.repository.cache;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface IListRedisRepository<T extends Serializable> extends IRedisObjectRepository<T> {

    void add(String keySuffix, T value);

    void addWithDefaultTimeout(String keySuffix, T value);

    void addWithTimeout(String keySuffix, T value, long timeout, TimeUnit timeUnit);

    Long addAll(String keySuffix, T[] values);

    Long addAllWithDefaultTimeout(String keySuffix, T[] values);

    Long addAllWithTimeout(String keySuffix, T[] values, long timeout, TimeUnit timeUnit);

    void expireWithDefaultTimeout(String keySuffix);

    void expire(String keySuffix, long timeout, TimeUnit timeUnit);

    List<T> get(String keySuffix);

    Long size(String keySuffix);
}
