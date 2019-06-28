package com.ugrong.framework.redis.domain;

public interface RedisLockType {

    String getLockKey();

    String getDesc();
}
