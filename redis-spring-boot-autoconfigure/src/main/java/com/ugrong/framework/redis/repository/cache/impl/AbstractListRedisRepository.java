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
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.util.Assert;

import com.ugrong.framework.redis.repository.cache.IListRedisRepository;

public abstract class AbstractListRedisRepository<T extends Serializable> extends AbstractRedisObjectRepository<T> implements IListRedisRepository<T> {

	private BoundListOperations<String, T> getListOperation(String keySuffix) {
		return super.geTemplate().boundListOps(super.getKey(keySuffix));
	}

	@Override
	public final void add(String keySuffix, T value) {
		Assert.notNull(value, "This redis value is required; it must not be null");
		this.getListOperation(keySuffix).rightPush(value);
	}

	@Override
	public void addWithDefaultTimeout(String keySuffix, T value) {
		this.addWithTimeout(keySuffix, value, 7, TimeUnit.DAYS);
	}

	@Override
	public final void addWithTimeout(String keySuffix, T value, long timeout, TimeUnit timeUnit) {
		Assert.notNull(value, "This redis value is required; it must not be null");
		this.validTimeArgs(timeout, timeUnit);
		BoundListOperations<String, T> operation = this.getListOperation(keySuffix);
		operation.rightPush(value);
		operation.expire(timeout, timeUnit);
	}

	@Override
	public final Long addAll(String keySuffix, T[] values) {
		Assert.notEmpty(values, "This redis values is required; it must not be empty");
		return this.getListOperation(keySuffix).rightPushAll(values);
	}

	@Override
	public Long addAllWithDefaultTimeout(String keySuffix, T[] values) {
		return this.addAllWithTimeout(keySuffix, values, 7, TimeUnit.DAYS);
	}

	@Override
	public final Long addAllWithTimeout(String keySuffix, T[] values, long timeout, TimeUnit timeUnit) {
		Assert.notEmpty(values, "This redis values is required; it must not be empty");
		this.validTimeArgs(timeout, timeUnit);
		BoundListOperations<String, T> operation = this.getListOperation(keySuffix);
		Long count = operation.rightPushAll(values);
		operation.expire(timeout, timeUnit);
		return count;
	}

	@Override
	public void expireWithDefaultTimeout(String keySuffix) {
		this.expire(keySuffix, 7, TimeUnit.DAYS);
	}

	@Override
	public final void expire(String keySuffix, long timeout, TimeUnit timeUnit) {
		this.getListOperation(keySuffix).expire(timeout, timeUnit);
	}

	@Override
	public List<T> get(String keySuffix) {
		return this.getListOperation(keySuffix).range(0, -1);
	}

	@Override
	public Long size(String keySuffix) {
		return this.getListOperation(keySuffix).size();
	}
}
