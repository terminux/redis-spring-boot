package com.ugrong.framework.redis.domain;

public interface IRedisLockType extends IRedisType {

    default String getType() {
        return "LOCK";
    }

}
