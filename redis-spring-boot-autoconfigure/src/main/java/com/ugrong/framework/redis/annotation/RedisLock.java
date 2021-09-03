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

package com.ugrong.framework.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import com.ugrong.framework.redis.repository.lock.impl.RedissonLockImpl;

/**
 * The interface Redis lock.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisLock {

	/**
	 * 锁的类型
	 *
	 * @return lock type
	 */
	String lockType();

	/**
	 * 要锁的字段
	 *
	 * @return lock field
	 */
	String lockField();

	/**
	 * 等待时间，默认10秒
	 *
	 * @return wait time
	 */
	long waitTime() default RedissonLockImpl.DEFAULT_LOCK_WAIT_TIME;

	/**
	 * 持有锁的时间，默认30秒
	 *
	 * @return timeout
	 */
	long timeout() default RedissonLockImpl.DEFAULT_LOCK_TIMEOUT;

	/**
	 * 时间单位
	 *
	 * @return time unit
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

}
