package com.ugrong.framework.redis.repository;

public interface IRedisRepository {

	String DEFAULT_REDIS_KEY_DELIMITER = ":";

	default String getKeyDelimiter() {
		return DEFAULT_REDIS_KEY_DELIMITER;
	}

	String REDIS_KEY_PATTERN = "*";
}
