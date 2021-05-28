package com.ugrong.framework.redis.samples.service.impl;

import com.ugrong.framework.redis.annotation.RedisLock;
import com.ugrong.framework.redis.samples.service.IStudentService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class IStudentServiceImpl implements IStudentService {

    @Override
    @RedisLock(lockType = "STUDENT_LOCK", lockField = "123")
    public void testAnonLock() {
        System.out.println("获取到锁，当前线程名称：" + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
