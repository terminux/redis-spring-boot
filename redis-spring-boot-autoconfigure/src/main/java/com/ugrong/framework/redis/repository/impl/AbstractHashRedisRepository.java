package com.ugrong.framework.redis.repository.impl;

import com.ugrong.framework.redis.repository.CollectionRedisRepository;
import org.springframework.data.redis.core.BoundHashOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractHashRedisRepository<T> extends BaseRedisRepositoryImpl<T> implements CollectionRedisRepository<T> {

    @Override
    public BoundHashOperations<String, String, T> getHashOps(String keySuffix) {
        return super.getRedisTemplate().boundHashOps(getKey(this.getKeyPrefix(), keySuffix));
    }

    @Override
    public void set(String keySuffix, String field, T value) {
        this.getHashOps(keySuffix).put(field, value);
    }

    @Override
    public void setWithDefaultTimeout(String keySuffix, String field, T value) {
        this.setWithTimeOut(keySuffix, field, value, 7, TimeUnit.DAYS);
    }

    @Override
    public void setWithTimeOut(String keySuffix, String field, T value, long timeOut, TimeUnit timeUnit) {
        preSetProcess(value);
        BoundHashOperations<String, String, T> operations = this.getHashOps(keySuffix);
        operations.expire(timeOut, timeUnit);
        operations.put(field, value);
    }

    @Override
    public void remove(String keySuffix, String field) {
        this.getHashOps(keySuffix).delete(field);
    }

    @Override
    public T get(String keySuffix, String field) {
        T value = this.getHashOps(keySuffix).get(field);
        postGetProcess(value);
        return value;
    }

    @Override
    public List<T> get(String keySuffix) {
        return this.getHashOps(keySuffix).values();
    }

    @Override
    public Boolean hasKey(String keySuffix) {
        return super.hasKey(this.getKeyPrefix(), keySuffix);
    }

    @Override
    public void preSetProcess(T value) {

    }

    @Override
    public void postGetProcess(T value) {

    }
}
