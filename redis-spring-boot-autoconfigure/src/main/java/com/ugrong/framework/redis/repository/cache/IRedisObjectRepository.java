package com.ugrong.framework.redis.repository.cache;


import com.ugrong.framework.redis.domain.IRedisCacheType;

import java.io.Serializable;
import java.util.Set;

public interface IRedisObjectRepository<T extends Serializable> extends IRedisCacheRepository<T> {

    IRedisCacheType getCacheType();

    String getKey(String keySuffix);

    String getKeyPrefix();

    Boolean remove(String keySuffix);

    Boolean hasKey(String keySuffix);

    Long getExpire(String keySuffix);

    Set<String> keys();
}
