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

package com.ugrong.framework.redis.repository.lock.aop;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.Assert;

import com.ugrong.framework.redis.annotation.RedisLock;
import com.ugrong.framework.redis.domain.IRedisLockType;
import com.ugrong.framework.redis.repository.lock.IRedisLockRepository;

@Aspect
@Slf4j
public class RedisLockAspect {

	private final IRedisLockRepository redisLockRepository;

	public RedisLockAspect(IRedisLockRepository redisLockRepository) {
		this.redisLockRepository = redisLockRepository;
	}

	@Pointcut("@annotation(com.ugrong.framework.redis.annotation.RedisLock)")
	public void redisLockPoint() {

	}

	@Around("redisLockPoint()")
	public Object processRedisLock(ProceedingJoinPoint joinPoint) throws Throwable {
		RedisLock redisLock = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(RedisLock.class);
		this.validRedisLock(redisLock);
		IRedisLockType lockType = redisLock::lockType;
		String lockField = redisLock.lockField();
		AtomicBoolean isLock = new AtomicBoolean(Boolean.FALSE);
		try {
			isLock.set(redisLockRepository
					.tryLock(lockType, lockField, redisLock.waitTime(), redisLock.timeout(), redisLock.timeUnit()));
			if (isLock.get()) {
				//获取到锁
				return joinPoint.proceed();
			}
			throw new IllegalArgumentException("Failed to get redis lock.");
		}
		catch (Exception e) {
			log.error("Failed to process redis lock.", e);
			throw e;
		}
		finally {
			//进行解锁
			if (isLock.get()) {
				redisLockRepository.unlock(lockType, lockField);
			}
		}
	}

	private void validRedisLock(RedisLock redisLock) {
		Assert.notNull(redisLock, "This redis lock field is required; it must not be null.");
		Assert.hasText(redisLock.lockType(), "This redis lock type is required; it must not be null.");
		Assert.hasText(redisLock.lockField(), "This redis lock field is required; it must not be null.");
		Assert.isTrue(redisLock.waitTime() >= 0 && redisLock
				.timeout() > 0, "This [timeout] and [waitTime] is required; it must not be null");
	}
}
