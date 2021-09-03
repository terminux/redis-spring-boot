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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.util.Assert;

import com.ugrong.framework.redis.repository.cache.IHashRedisRepository;

public abstract class AbstractHashRedisRepository<K, V extends Serializable> extends AbstractRedisObjectRepository<V> implements IHashRedisRepository<K, V> {

	private BoundHashOperations<String, K, V> getHashOperation(String keySuffix) {
		return super.geTemplate().boundHashOps(super.getKey(keySuffix));
	}

	@Override
	public void put(String keySuffix, K field, V value) {
		this.validArgs(field, value);
		this.getHashOperation(keySuffix).put(field, value);
	}

	private void validArgs(K field, V value) {
		Assert.notNull(field, "This redis hash key is required; it must not be null");
		Assert.notNull(value, "This redis hash value is required; it must not be null");
	}

	@Override
	public void putWithDefaultTimeout(String keySuffix, K field, V value) {
		this.putWithTimeout(keySuffix, field, value, 7, TimeUnit.DAYS);
	}

	@Override
	public void putWithTimeout(String keySuffix, K field, V value, long timeout, TimeUnit timeUnit) {
		this.validArgs(field, value);
		this.validTimeArgs(timeout, timeUnit);
		BoundHashOperations<String, K, V> operation = this.getHashOperation(keySuffix);
		operation.put(field, value);
		operation.expire(timeout, timeUnit);
	}

	@Override
	public void putAll(String keySuffix, Map<? extends K, ? extends V> map) {
		Assert.notEmpty(map, "This redis hash data map is required; it must not be null");
		this.getHashOperation(keySuffix).putAll(map);
	}

	@Override
	public void putAllWithDefaultTimeout(String keySuffix, Map<? extends K, ? extends V> map) {
		this.putAllWithTimeout(keySuffix, map, 7, TimeUnit.DAYS);
	}

	@Override
	public void putAllWithTimeout(String keySuffix, Map<? extends K, ? extends V> map, long timeout, TimeUnit timeUnit) {
		Assert.notEmpty(map, "This redis hash data map is required; it must not be null");
		this.validTimeArgs(timeout, timeUnit);
		BoundHashOperations<String, K, V> operation = this.getHashOperation(keySuffix);
		operation.putAll(map);
		operation.expire(timeout, timeUnit);
	}

	@Override
	public void remove(String keySuffix, K field) {
		Assert.notNull(field, "This redis hash key is required; it must not be null");
		this.getHashOperation(keySuffix).delete(field);
	}

	@Override
	public Optional<V> get(String keySuffix, K field) {
		Assert.notNull(field, "This redis hash key is required; it must not be null");
		return Optional.ofNullable(this.getHashOperation(keySuffix).get(field));
	}

	@Override
	public List<V> getAll(String keySuffix) {
		return this.getHashOperation(keySuffix).values();
	}

	@Override
	public Boolean hasField(String keySuffix, K field) {
		Assert.notNull(field, "This redis hash key is required; it must not be null");
		return this.getHashOperation(keySuffix).hasKey(field);
	}

	@Override
	public Set<K> fields(String keySuffix) {
		return this.getHashOperation(keySuffix).keys();
	}

	@Override
	public Map<K, V> entries(String keySuffix) {
		return this.getHashOperation(keySuffix).entries();
	}

	@Override
	public Long getAndIncrement(String keySuffix, K field) {
		return this.getAndIncrementBy(keySuffix, field, 1L);
	}

	@Override
	public Long getAndIncrementBy(String keySuffix, K field, Long incrementBy) {
		Assert.notNull(field, "This redis hash key is required; it must not be null");
		Assert.notNull(incrementBy, "This [incrementBy] is required; it must not be null");
		return this.getHashOperation(keySuffix).increment(field, incrementBy);
	}
}
