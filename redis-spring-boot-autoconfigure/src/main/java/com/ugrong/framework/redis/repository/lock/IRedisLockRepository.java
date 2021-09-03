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

package com.ugrong.framework.redis.repository.lock;

import java.util.concurrent.TimeUnit;

import com.ugrong.framework.redis.domain.IRedisLockType;
import com.ugrong.framework.redis.repository.IRedisRepository;

/**
 * redis分布式锁 支持对某个key进行细粒度加锁.
 */
public interface IRedisLockRepository extends IRedisRepository {

	/**
	 * 获取锁的完整key.
	 *
	 * @param lockType  锁的类型 用于redis key前缀
	 * @param lockField 要锁的字段 用于redis key后缀
	 * @return the lock key
	 */
	String getLockKey(IRedisLockType lockType, String lockField);

	/**
	 * 尝试获取锁，返回获取状态；
	 * 若获取失败将立即返回false，否则立即返回true，并且锁的默认持有时间30秒；
	 *
	 * @param lockType  锁的类型 用于redis key前缀
	 * @param lockField 要锁的字段 用于redis key后缀
	 * @return 是否加锁成功
	 */
	boolean lockWithDefaultTimeout(IRedisLockType lockType, String lockField);

	/**
	 * 尝试获取锁，返回获取状态；
	 * 与 {@link IRedisLockRepository#lockWithDefaultTimeout(IRedisLockType, String)} 类似，且支持自定义锁的持有时间
	 *
	 * @param lockType  锁的类型 用于redis key前缀
	 * @param lockField 要锁的字段 用于redis key后缀
	 * @param timeout   持有锁的时间
	 * @param timeUnit  持有锁的时间单位
	 * @return 是否加锁成功
	 */
	boolean lockWithTimeout(IRedisLockType lockType, String lockField, long timeout, TimeUnit timeUnit);

	/**
	 * 尝试获取锁，如果获取成功，则锁的默认持有时间30秒，且返回true，
	 * 如果获取失败（即锁已被其他线程获取），会默认等待10秒，在10秒内如果还拿不到锁，则返回false
	 *
	 * @param lockType  锁的类型 用于redis key前缀
	 * @param lockField 要锁的字段 用于redis key后缀
	 * @return 是否加锁成功
	 */
	boolean tryLockWithDefaultTimeout(IRedisLockType lockType, String lockField);

	/**
	 * 与 {@link IRedisLockRepository#tryLockWithDefaultTimeout(IRedisLockType, String)} 类似，且支持自定义锁的等待和持有时间
	 *
	 * @param lockType  锁的类型 用于redis key前缀
	 * @param lockField 要锁的字段 用于redis key后缀
	 * @param waitTime  等待时间
	 * @param timeout   持有锁的时间
	 * @param timeUnit  时间单位
	 * @return 是否加锁成功
	 */
	boolean tryLock(IRedisLockType lockType, String lockField, long waitTime, long timeout, TimeUnit timeUnit);

	/**
	 * 释放锁
	 *
	 * @param lockType  锁的类型 用于redis key前缀
	 * @param lockField 要锁的字段 用于redis key后缀
	 * @return 是否释放成功
	 */
	boolean unlock(IRedisLockType lockType, String lockField);
}
