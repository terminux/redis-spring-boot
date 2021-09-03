package com.ugrong.framework.redis.samples.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.ugrong.framework.redis.annotation.RedisHandler;
import com.ugrong.framework.redis.handler.IRedisMessageHandler;
import com.ugrong.framework.redis.samples.constants.SamplesConstants;
import com.ugrong.framework.redis.samples.model.Student;

@Component
@RedisHandler(topic = SamplesConstants.STUDENT_TOPIC_VALUE)
@Slf4j
public class StudentMessageHandler implements IRedisMessageHandler<Student> {

	@Override
	public void handle(Student student, String topic) {
		log.info("Handle redis message.topic=[{}], received=[{}]", topic, student);
	}
}