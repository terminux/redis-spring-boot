package com.ugrong.framework.redis.model;

import com.ugrong.framework.redis.domain.RedisKeyPrefix;

public enum RedisLockKeyPrefix implements RedisKeyPrefix {

    LOCK("通用锁前缀");

    private final String desc;

    RedisLockKeyPrefix(String desc) {
        this.desc = desc;
    }

    @Override
    public String getPrefix() {
        return this.name();
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

}
