package com.ugrong.framework.redis.samples.type;

import com.ugrong.framework.redis.domain.IRedisTopicType;
import com.ugrong.framework.redis.samples.constants.SamplesConstants;

public enum EnumStudentTopicType implements IRedisTopicType {

	STUDENT_TOPIC(SamplesConstants.STUDENT_TOPIC_VALUE);

	private final String value;

	EnumStudentTopicType(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
}