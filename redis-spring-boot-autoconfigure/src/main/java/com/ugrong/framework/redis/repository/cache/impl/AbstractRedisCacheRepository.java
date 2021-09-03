/**
 * MIT License
 *
 * Copyright (c) 2019-2021 ugrong@163.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ugrong.framework.redis.repository.cache.impl;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

import com.ugrong.framework.redis.domain.IRedisCacheType;
import com.ugrong.framework.redis.repository.AbstractRedisRepository;
import com.ugrong.framework.redis.repository.cache.IRedisCacheRepository;

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
