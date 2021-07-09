package com.ugrong.framework.redis.domain;

public interface IRedisTopicType extends IRedisType {

	String DEFAULT_REDIS_TOPIC_TYPE = "/topic";

	default String getType() {
		return DEFAULT_REDIS_TOPIC_TYPE;
	}
}
