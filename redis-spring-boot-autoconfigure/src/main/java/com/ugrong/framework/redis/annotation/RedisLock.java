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