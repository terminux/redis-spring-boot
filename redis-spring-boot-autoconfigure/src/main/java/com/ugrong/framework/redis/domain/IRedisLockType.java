package com.ugrong.framework.redis.domain;

public interface IRedisLockType extends IRedisType {

    String DEFAULT_REDIS_LOCK_TYPE = "LOCK";

    default String getType() {
        return DEFAULT_REDIS_LOCK_TYPE;
    }

}
