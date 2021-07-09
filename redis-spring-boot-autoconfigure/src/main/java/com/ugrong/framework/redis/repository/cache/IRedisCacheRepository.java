package com.ugrong.framework.redis.repository.cache;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisTemplate;

import com.ugrong.framework.redis.repository.IRedisRepository;

public interface IRedisCacheRepository<T extends Serializable> extends IRedisRepository {

	RedisTemplate<String, T> geTemplate();

}
