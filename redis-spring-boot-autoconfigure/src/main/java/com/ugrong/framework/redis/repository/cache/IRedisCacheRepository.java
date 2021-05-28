package com.ugrong.framework.redis.repository.cache;

import com.ugrong.framework.redis.repository.IRedisRepository;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

public interface IRedisCacheRepository<T extends Serializable> extends IRedisRepository {

    RedisTemplate<String, T> geTemplate();

}
