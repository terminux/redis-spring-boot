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

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface IListRedisRepository<T extends Serializable> extends IRedisObjectRepository<T> {

	void add(String keySuffix, T value);

	void addWithDefaultTimeout(String keySuffix, T value);

	void addWithTimeout(String keySuffix, T value, long timeout, TimeUnit timeUnit);

	Long addAll(String keySuffix, T[] values);

	Long addAllWithDefaultTimeout(String keySuffix, T[] values);

	Long addAllWithTimeout(String keySuffix, T[] values, long timeout, TimeUnit timeUnit);

	void expireWithDefaultTimeout(String keySuffix);

	void expire(String keySuffix, long timeout, TimeUnit timeUnit);

	List<T> get(String keySuffix);

	Long size(String keySuffix);
}
