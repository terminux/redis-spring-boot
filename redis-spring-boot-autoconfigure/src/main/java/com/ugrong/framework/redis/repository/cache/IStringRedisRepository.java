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

package com.ugrong.framework.redis.repository.cache;


import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.ugrong.framework.redis.domain.IRedisCacheType;

public interface IStringRedisRepository extends IRedisCacheRepository<String> {

	void setStringValue(IRedisCacheType cacheType, String value);

	void setStringValue(IRedisCacheType cacheType, String suffix, String value);

	Optional<String> getString(IRedisCacheType cacheType);

	Optional<String> getString(IRedisCacheType cacheType, String suffix);

	void setStringValueWithTimeOut(IRedisCacheType cacheType, String suffix, String value, long timeout, TimeUnit timeUnit);

	void expire(IRedisCacheType cacheType, String suffix, long timeout, TimeUnit timeUnit);

	Boolean remove(IRedisCacheType cacheType);

	Boolean remove(IRedisCacheType cacheType, String suffix);

	Long getAndIncrement(IRedisCacheType cacheType);

	Long getAndIncrement(IRedisCacheType cacheType, String keySuffix);

	Long getAndIncrementBy(IRedisCacheType cacheType, Long incrementBy);

	Long getAndIncrementBy(IRedisCacheType cacheType, String keySuffix, Long incrementBy);

	Long getAndDecrement(IRedisCacheType cacheType);

	Long getAndDecrement(IRedisCacheType cacheType, String keySuffix);

	Long getAndDecrementBy(IRedisCacheType cacheType, Long decrementBy);

	Long getAndDecrementBy(IRedisCacheType cacheType, String keySuffix, Long decrementBy);
}
