package com.ugrong.framework.redis.repository.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface ISimpleRedisRepository<T extends Serializable> extends IRedisObjectRepository<T> {

	void set(String keySuffix, T value);

	Optional<T> get(String keySuffix);

	List<T> getAll();

	void setWithDefaultTimeout(String keySuffix, T value);

	void setWithTimeout(String keySuffix, T value, long timeout, TimeUnit timeUnit);

	void expireWithDefaultTimeout(String keySuffix);

	void expire(String keySuffix, long timeout, TimeUnit timeUnit);
}
