package com.ugrong.framework.redis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The interface Redis handler.
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface RedisHandler {

	/**
	 * 订阅的主题
	 *
	 * @return topic
	 */
	String topic();

}
