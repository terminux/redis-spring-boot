package com.ugrong.framework.redis.samples.type;

import com.ugrong.framework.redis.domain.IRedisTopicType;

public enum EnumStudentTopicType implements IRedisTopicType {

	STUDENT_TOPIC("student_topic");

	private final String value;

	EnumStudentTopicType(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
}