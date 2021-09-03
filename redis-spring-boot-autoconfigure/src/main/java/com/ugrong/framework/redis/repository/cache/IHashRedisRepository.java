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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface IHashRedisRepository<K, V extends Serializable> extends IRedisObjectRepository<V> {

	void put(String keySuffix, K field, V value);

	void putWithDefaultTimeout(String keySuffix, K field, V value);

	void putWithTimeout(String keySuffix, K field, V value, long timeout, TimeUnit timeUnit);

	void putAll(String keySuffix, Map<? extends K, ? extends V> map);

	void putAllWithDefaultTimeout(String keySuffix, Map<? extends K, ? extends V> map);

	void putAllWithTimeout(String keySuffix, Map<? extends K, ? extends V> map, long timeout, TimeUnit timeUnit);

	void remove(String keySuffix, K field);

	Optional<V> get(String keySuffix, K field);

	List<V> getAll(String keySuffix);

	Boolean hasField(String keySuffix, K field);

	Set<K> fields(String keySuffix);

	Map<K, V> entries(String keySuffix);

	Long getAndIncrement(String keySuffix, K field);

	Long getAndIncrementBy(String keySuffix, K field, Long incrementBy);
}
