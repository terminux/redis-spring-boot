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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;

import com.ugrong.framework.redis.repository.cache.ISimpleRedisRepository;

public abstract class AbstractSimpleRedisRepository<T extends Serializable> extends AbstractRedisObjectRepository<T> implements ISimpleRedisRepository<T> {

	private ValueOperations<String, T> getOperation() {
		return this.geTemplate().opsForValue();
	}

	@Override
	public final void set(String keySuffix, T value) {
		Assert.notNull(value, "This redis value is required; it must not be null");
		this.getOperation().set(this.getKey(keySuffix), value);
	}

	@Override
	public final Optional<T> get(String keySuffix) {
		return Optional.ofNullable(this.getOperation().get(this.getKey(keySuffix)));
	}

	@Override
	public List<T> getAll() {
		Set<String> keys = this.keys();
		if (CollectionUtils.isNotEmpty(keys)) {
			return this.getOperation().multiGet(keys);
		}
		return Lists.newArrayList();
	}

	@Override
	public void setWithDefaultTimeout(String keySuffix, T value) {
		this.setWithTimeout(keySuffix, value, 7, TimeUnit.DAYS);
	}

	@Override
	public void setWithTimeout(String keySuffix, T value, long timeout, TimeUnit timeUnit) {
		Assert.notNull(value, "This redis value is required; it must not be null");
		this.validTimeArgs(timeout, timeUnit);
		this.getOperation().set(this.getKey(keySuffix), value, timeout, timeUnit);
	}

	@Override
	public void expireWithDefaultTimeout(String keySuffix) {
		this.expire(keySuffix, 7, TimeUnit.DAYS);
	}

	@Override
	public final void expire(String keySuffix, long timeout, TimeUnit timeUnit) {
		super.expire(this.getCacheType(), keySuffix, timeout, timeUnit);
	}
}
