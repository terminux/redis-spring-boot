package com.ugrong.framework.redis.repository.cache.impl;


import java.io.Serializable;
import java.util.Set;

import com.ugrong.framework.redis.repository.cache.IRedisObjectRepository;

public abstract class AbstractRedisObjectRepository<T extends Serializable> extends AbstractRedisCacheRepository<T> implements IRedisObjectRepository<T> {

	@Override
	public final String getKey(String keySuffix) {
		return this.getKey(this.getCacheType(), keySuffix);
	}

	@Override
	public String getKeyPrefix() {
		return this.getKeyPrefix(this.getCacheType());
	}

	@Override
	public final Boolean remove(String keySuffix) {
		return super.remove(this.getCacheType(), keySuffix);
	}

	@Override
	public final Boolean hasKey(String keySuffix) {
		return super.hasKey(this.getCacheType(), keySuffix);
	}

	@Override
	public final Long getExpire(String keySuffix) {
		return super.getExpire(this.getCacheType(), keySuffix);
	}

	@Override
	public Set<String> keys() {
		return super.keys(this.getCacheType());
	}
}
