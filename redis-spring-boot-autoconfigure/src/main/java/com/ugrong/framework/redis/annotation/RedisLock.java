package com.ugrong.framework.redis.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisLock {

    /**
     * 锁的field
     *
     * @return
     */
    String lockField();

    /**
     * 锁的超时时间 单位：毫秒 默认30秒
     *
     * @return
     */
    long timeoutMills() default 30000;

    /**
     * 重试的时间间隔(秒)
     *
     * @return
     */
    long tryIntervalSeconds() default 5;

    /**
     * 最大的重试次数
     *
     * @return
     */
    int maxTryCount() default 5;

}