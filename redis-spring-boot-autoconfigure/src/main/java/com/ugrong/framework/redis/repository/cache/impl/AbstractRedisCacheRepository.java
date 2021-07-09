package com.ugrong.framework.redis.repository.cache.impl;

import com.ugrong.framework.redis.domain.IRedisCacheType;
import com.ugrong.framework.redis.repository.AbstractRedisRepository;
import com.ugrong.framework.redis.repository.cache.IRedisCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRedisCacheRepository<T extends Serializable> extends AbstractRedisRepository<IRedisCacheType> implements IRedisCacheRepository<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    @Override
    public final RedisTemplate<String, T> geTemplate() {
        return this.redisTemplate;
    }

    protected final Boolean hasKey(IRedisCacheType cacheType, String keySuffix) {
        return this.hasKey(this.getKey(cacheType, keySuffix));
    }

    private Boolean hasKey(String key) {
        Boolean hasKey = this.geTemplate().hasKey(key);
        return hasKey == null ? Boolean.FALSE : hasKey;
    }

    protected final Set<String> keys(IRedisCacheType cacheType) {
        return this.geTemplate()
                .keys(this.getKey(cacheType, null).concat(this.getKeyDelimiter()).concat(REDIS_KEY_PATTERN));
    }

    protected void expire(IRedisCacheType cacheType, String suffix, long timeout, TimeUnit timeUnit) {
        this.validTimeArgs(timeout, timeUnit);
        String key = this.getKey(cacheType, suffix);
        if (this.hasKey(key)) {
            this.geTemplate().expire(key, timeout, timeUnit);
        }
    }

    protected final Long getExpire(IRedisCacheType cacheType, String suffix) {
        return this.geTemplate().getExpire(this.getKey(cacheType, suffix));
    }

    protected Boolean remove(IRedisCacheType cacheType, String suffix) {
        return this.geTemplate().delete(this.getKey(cacheType, suffix));
    }
}
