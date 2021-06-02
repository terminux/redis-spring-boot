package com.ugrong.framework.redis.domain;

public interface IRedisCacheType extends IRedisType {

    String DEFAULT_REDIS_CACHE_TYPE = "CACHE";

    default String getType() {
        return DEFAULT_REDIS_CACHE_TYPE;
    }
}
