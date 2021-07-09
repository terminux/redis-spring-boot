package com.ugrong.framework.redis.repository.cache;


import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.ugrong.framework.redis.domain.IRedisCacheType;

public interface IStringRedisRepository extends IRedisCacheRepository<String> {

	void setStringValue(IRedisCacheType cacheType, String value);

	void setStringValue(IRedisCacheType cacheType, String suffix, String value);

	Optional<String> getString(IRedisCacheType cacheType);

	Optional<String> getString(IRedisCacheType cacheType, String suffix);

	void setStringValueWithTimeOut(IRedisCacheType cacheType, String suffix, String value, long timeout, TimeUnit timeUnit);

	void expire(IRedisCacheType cacheType, String suffix, long timeout, TimeUnit timeUnit);

	Boolean remove(IRedisCacheType cacheType);

	Boolean remove(IRedisCacheType cacheType, String suffix);

	Long getAndIncrement(IRedisCacheType cacheType);

	Long getAndIncrement(IRedisCacheType cacheType, String keySuffix);

	Long getAndIncrementBy(IRedisCacheType cacheType, Long incrementBy);

	Long getAndIncrementBy(IRedisCacheType cacheType, String keySuffix, Long incrementBy);

	Long getAndDecrement(IRedisCacheType cacheType);

	Long getAndDecrement(IRedisCacheType cacheType, String keySuffix);

	Long getAndDecrementBy(IRedisCacheType cacheType, Long decrementBy);

	Long getAndDecrementBy(IRedisCacheType cacheType, String keySuffix, Long decrementBy);
}
