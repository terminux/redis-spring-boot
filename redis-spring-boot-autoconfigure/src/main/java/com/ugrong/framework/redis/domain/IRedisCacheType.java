package com.ugrong.framework.redis.domain;

public interface IRedisCacheType extends IRedisType {

    default String getType() {
        return "CACHE";
    }
}
