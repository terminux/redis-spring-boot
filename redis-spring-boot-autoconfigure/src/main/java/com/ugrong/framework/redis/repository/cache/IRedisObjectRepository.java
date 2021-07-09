package com.ugrong.framework.redis.repository.cache;


import java.io.Serializable;
import java.util.Set;

import com.ugrong.framework.redis.domain.IRedisCacheType;

public interface IRedisObjectRepository<T extends Serializable> extends IRedisCacheRepository<T> {

	IRedisCacheType getCacheType();

	String getKey(String keySuffix);

	String getKeyPrefix();

	Boolean remove(String keySuffix);

	Boolean hasKey(String keySuffix);

	Long getExpire(String keySuffix);

	Set<String> keys();
}
