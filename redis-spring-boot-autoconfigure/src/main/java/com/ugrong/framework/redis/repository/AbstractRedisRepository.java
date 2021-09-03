package com.ugrong.framework.redis.repository;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.ugrong.framework.redis.domain.IRedisType;
import com.ugrong.framework.redis.utils.RedisKeyUtil;

public abstract class AbstractRedisRepository<T extends IRedisType> implements IRedisRepository {

	protected void validTimeArgs(long timeout, TimeUnit timeUnit) {
		Assert.isTrue(timeout > 0 && timeUnit != null, "This [timeout] and [timeUnit] is required; it must not be null");
	}

	protected final String getKey(T type, String keySuffix) {
		this.validType(type);
		String keyDelimiter = this.getKeyDelimiter();
		String key = RedisKeyUtil.concatKey(type.getType(), keyDelimiter, type.getValue());
		if (StringUtils.isBlank(keySuffix)) {
			return key;
		}
		return RedisKeyUtil.concatKey(key, keyDelimiter, keySuffix);
	}

	protected void validType(T type) {
		Assert.isTrue(type != null && !StringUtils.isAnyBlank(type.getType(), type.getValue()),
				"This redis type and value is required; it must not be null");
	}
}
