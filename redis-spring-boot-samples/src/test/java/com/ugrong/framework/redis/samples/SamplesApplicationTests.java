package com.ugrong.framework.redis.samples;

import com.ugrong.framework.redis.repository.cache.IStringRedisRepository;
import com.ugrong.framework.redis.repository.channel.IRedisChannelRepository;
import com.ugrong.framework.redis.repository.lock.IRedisLockRepository;
import com.ugrong.framework.redis.samples.model.Student;
import com.ugrong.framework.redis.samples.repository.StudentRedisRepository;
import com.ugrong.framework.redis.samples.service.IStudentService;
import com.ugrong.framework.redis.samples.type.EnumStudentCacheType;
import com.ugrong.framework.redis.samples.type.EnumStudentLockType;
import com.ugrong.framework.redis.samples.type.EnumStudentTopicType;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class SamplesApplicationTests {

    @Autowired
    private IStringRedisRepository stringRedisRepository;

    @Autowired
    private StudentRedisRepository studentRedisRepository;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private IRedisLockRepository redisLockRepository;

    @Autowired
    private IRedisChannelRepository redisChannelRepository;

    private Student getStudent() {
        Student student = new Student();
        student.setId(RandomUtils.nextLong());
        student.setName("张三");
        return student;
    }

    @Test
    void redisSimpleCache() {
        Student student = this.getStudent();
        String id = student.getId().toString();

        studentRedisRepository.set(id, student);
        System.out.println(studentRedisRepository.get(id).orElse(null));

        studentRedisRepository.remove(id);
    }

    @Test
    void redisStringCache() {
        Student student = this.getStudent();
        String id = student.getId().toString();

        stringRedisRepository.setStringValue(EnumStudentCacheType.STUDENT_CACHE, id);
        System.out.println(stringRedisRepository.getString(EnumStudentCacheType.STUDENT_CACHE).orElse(null));

        stringRedisRepository.remove(EnumStudentCacheType.STUDENT_CACHE, id);

        //计数
        Long increment = stringRedisRepository.getAndIncrement(EnumStudentCacheType.STUDENT_CACHE, "xxx");
        System.out.println(increment);

        stringRedisRepository.remove(EnumStudentCacheType.STUDENT_CACHE, "xxx");
    }


    /**
     * 模拟了两个线程同时对一个学生进行加锁
     */
    @Test
    void redisLock() throws InterruptedException {
        Student student = this.getStudent();
        final String id = student.getId().toString();

        this.tryLock(id);

        new Thread(() -> this.tryLock(id)).start();

        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * 注解式redis锁
     */
    @Test
    public void redisAnonLock() {
        studentService.testAnonLock();
    }

    /**
     * 发布消息
     */
    @Test
    public void publishMessage() {
        Student student = this.getStudent();
        redisChannelRepository.publish(EnumStudentTopicType.STUDENT_TOPIC, student);
    }

    private void tryLock(String lockField) {
        try {
            if (redisLockRepository.tryLock(EnumStudentLockType.STUDENT_LOCK, lockField, 20, 20, TimeUnit.SECONDS)) {
                System.out.println("获取到锁，当前线程名称：" + Thread.currentThread().getName());
                //休息3秒 模拟业务处理
                TimeUnit.SECONDS.sleep(3);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLockRepository.unlock(EnumStudentLockType.STUDENT_LOCK, lockField);
        }
    }

}
