package com.ugrong.framework.redis.repository;

import org.springframework.data.redis.core.BoundListOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface ListRedisRepository<T> extends SimpleRedisRepository<T> {

    BoundListOperations<String, T> getListOps(String keySuffix);

    void set(String keySuffix, T[] values);

    void setWithDefaultTimeOut(String keySuffix, T[] values);

    void setWithTimeOut(String keySuffix, T[] values, long timeout, TimeUnit timeUnit);

    List<T> getAll(String keySuffix);

    void preSetProcess(T[] values);
}
