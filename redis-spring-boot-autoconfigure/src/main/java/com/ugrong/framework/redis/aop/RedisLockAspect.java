package com.ugrong.framework.redis.aop;

import com.ugrong.framework.redis.annotation.RedisLock;
import com.ugrong.framework.redis.lock.service.RedisLockService;
import com.ugrong.framework.redis.model.RedisDefaultLockType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Aspect
@Slf4j
public class RedisLockAspect {

    @Autowired
    private RedisLockService redisLockService;

    @Pointcut("@annotation(com.ugrong.framework.redis.annotation.RedisLock)")
    public void redisLockPoint() {

    }

    @Around("redisLockPoint()")
    public Object processRedisLock(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            RedisLock redisLock = AnnotationUtils.findAnnotation(((MethodSignature) joinPoint.getSignature()).getMethod(),
                    RedisLock.class);
            validateRedisLock(redisLock);

            AtomicReference<String> lockValue = new AtomicReference<>(UUID.randomUUID().toString());

            //使用了默认的锁类型
            boolean locked = redisLockService.tryLock(RedisDefaultLockType.DEFAULT_TYPE, redisLock.lockField(),
                    lockValue.get(), redisLock.timeoutMills(), TimeUnit.MILLISECONDS,
                    redisLock.tryIntervalSeconds(), redisLock.maxTryCount());
            Assert.isTrue(locked, "Failed to get redis lock.");

            Object returnValue = joinPoint.proceed();

            boolean unlocked = redisLockService.unlock(RedisDefaultLockType.DEFAULT_TYPE,
                    redisLock.lockField(), lockValue.get());
            Assert.isTrue(unlocked, "Failed to release redis lock.");

            return returnValue;
        } catch (Exception e) {
            log.error("Failed to process redis lock.{}", e);
            throw new RuntimeException(e);
        }
    }

    private void validateRedisLock(RedisLock redisLock) {
        Assert.isTrue(redisLock != null && StringUtils.hasText(redisLock.lockField()),
                "This redis lock field is required; it must not be null.");
    }

}
