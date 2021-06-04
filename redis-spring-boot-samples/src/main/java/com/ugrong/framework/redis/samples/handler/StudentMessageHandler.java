package com.ugrong.framework.redis.samples.handler;

import com.ugrong.framework.redis.annotation.RedisHandler;
import com.ugrong.framework.redis.handler.RedisMessageHandler;
import com.ugrong.framework.redis.samples.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RedisHandler(topic = "student_topic")
@Slf4j
public class StudentMessageHandler implements RedisMessageHandler<Student> {

    @Override
    public void handle(Student student, String topic) {
        log.info("Handle redis message.topic=[{}], received=[{}]", topic, student);
    }
}