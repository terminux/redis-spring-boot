package com.ugrong.framework.redis.samples.type;

import com.ugrong.framework.redis.domain.IRedisLockType;

public enum EnumStudentLockType implements IRedisLockType {

	STUDENT_LOCK("STUDENT_LOCK");

	private final String value;

	EnumStudentLockType(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
}