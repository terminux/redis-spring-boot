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

import com.ugrong.framework.redis.domain.IRedisCacheType;
import com.ugrong.framework.redis.repository.cache.IRedisObjectRepository;

public abstract class AbstractRedisObjectRepository<T extends Serializable> extends AbstractRedisCacheRepository<T> implements IRedisObjectRepository<T> {

	@Override
	public final String getKey(String keySuffix) {
		return this.getKey(this.getCacheType(), keySuffix);
	}

	@Override
	public String getKeyPrefix() {
		IRedisCacheType cacheType = this.getCacheType();
		super.validType(cacheType);
		return cacheType.getType().concat(this.getKeyDelimiter());
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
