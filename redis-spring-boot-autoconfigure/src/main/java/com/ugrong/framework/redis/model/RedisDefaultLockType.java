package com.ugrong.framework.redis.model;

import com.ugrong.framework.redis.domain.RedisLockType;

public enum RedisDefaultLockType implements RedisLockType {

    DEFAULT_TYPE("通用锁类型");

    private final String desc;

    RedisDefaultLockType(String desc) {
        this.desc = desc;
    }

    @Override
    public String getLockKey() {
        return this.name();
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
