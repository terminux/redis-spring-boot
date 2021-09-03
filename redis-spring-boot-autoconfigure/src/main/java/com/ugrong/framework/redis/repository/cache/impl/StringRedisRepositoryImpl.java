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

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;

import com.ugrong.framework.redis.domain.IRedisCacheType;
import com.ugrong.framework.redis.repository.cache.IStringRedisRepository;

public class StringRedisRepositoryImpl extends AbstractRedisCacheRepository<String> implements IStringRedisRepository {

	private ValueOperations<String, String> getOperation() {
		return this.geTemplate().opsForValue();
	}

	@Override
	public void setStringValue(IRedisCacheType cacheType, String value) {
		this.setStringValue(cacheType, null, value);
	}

	@Override
	public void setStringValue(IRedisCacheType cacheType, String suffix, String value) {
		Assert.hasText(value, "This redis value is required; it must not be null");
		this.getOperation().set(super.getKey(cacheType, suffix), value);
	}

	@Override
	public Optional<String> getString(IRedisCacheType cacheType) {
		return this.getString(cacheType, null);
	}

	@Override
	public Optional<String> getString(IRedisCacheType cacheType, String suffix) {
		return Optional.ofNullable(this.getOperation().get(super.getKey(cacheType, suffix)));
	}

	@Override
	public void setStringValueWithTimeOut(IRedisCacheType cacheType, String suffix, String value, long timeout, TimeUnit timeUnit) {
		Assert.hasText(value, "This redis value is required; it must not be null");
		this.validTimeArgs(timeout, timeUnit);
		this.getOperation().set(super.getKey(cacheType, suffix), value, timeout, timeUnit);
	}

	@Override
	public void expire(IRedisCacheType cacheType, String suffix, long timeout, TimeUnit timeUnit) {
		super.expire(cacheType, suffix, timeout, timeUnit);
	}

	@Override
	public Boolean remove(IRedisCacheType cacheType) {
		return this.remove(cacheType, null);
	}

	@Override
	public Boolean remove(IRedisCacheType cacheType, String suffix) {
		return super.remove(cacheType, suffix);
	}

	@Override
	public Long getAndIncrement(IRedisCacheType cacheType) {
		return this.getAndIncrement(cacheType, null);
	}

	@Override
	public Long getAndIncrement(IRedisCacheType cacheType, String keySuffix) {
		return this.getOperation().increment(this.getKey(cacheType, keySuffix));
	}

	@Override
	public Long getAndIncrementBy(IRedisCacheType cacheType, Long incrementBy) {
		return getAndIncrementBy(cacheType, null, incrementBy);
	}

	@Override
	public Long getAndIncrementBy(IRedisCacheType cacheType, String keySuffix, Long incrementBy) {
		Assert.notNull(incrementBy, "This [incrementBy] is required; it must not be null");
		return this.getOperation().increment(this.getKey(cacheType, keySuffix), incrementBy);
	}

	@Override
	public Long getAndDecrement(IRedisCacheType cacheType) {
		return this.getAndDecrement(cacheType, null);
	}

	@Override
	public Long getAndDecrement(IRedisCacheType cacheType, String keySuffix) {
		return this.getOperation().decrement(this.getKey(cacheType, keySuffix));
	}

	@Override
	public Long getAndDecrementBy(IRedisCacheType cacheType, Long decrementBy) {
		return this.getAndDecrementBy(cacheType, null, decrementBy);
	}

	@Override
	public Long getAndDecrementBy(IRedisCacheType cacheType, String keySuffix, Long decrementBy) {
		Assert.notNull(decrementBy, "This [decrementBy] is required; it must not be null");
		return this.getOperation().decrement(this.getKey(cacheType, keySuffix), decrementBy);
	}
}
