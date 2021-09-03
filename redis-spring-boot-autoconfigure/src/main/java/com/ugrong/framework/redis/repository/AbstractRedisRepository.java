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

package com.ugrong.framework.redis.repository;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.ugrong.framework.redis.domain.IRedisType;
import com.ugrong.framework.redis.utils.RedisKeyUtil;

public abstract class AbstractRedisRepository<T extends IRedisType> implements IRedisRepository {

	protected void validTimeArgs(long timeout, TimeUnit timeUnit) {
		Assert.isTrue(timeout > 0 && timeUnit != null, "This [timeout] and [timeUnit] is required; it must not be null");
	}

	protected final String getKey(T type, String keySuffix) {
		this.validType(type);
		String keyDelimiter = this.getKeyDelimiter();
		String key = RedisKeyUtil.concatKey(type.getType(), keyDelimiter, type.getValue());
		if (StringUtils.isBlank(keySuffix)) {
			return key;
		}
		return RedisKeyUtil.concatKey(key, keyDelimiter, keySuffix);
	}

	protected void validType(T type) {
		Assert.isTrue(type != null && !StringUtils.isAnyBlank(type.getType(), type.getValue()),
				"This redis type and value is required; it must not be null");
	}
}
