package com.ugrong.framework.redis.samples.type;


import com.ugrong.framework.redis.domain.IRedisCacheType;

public enum EnumStudentCacheType implements IRedisCacheType {

    /**
     * 学生信息缓存
     */
    STUDENT_CACHE("STUDENT");

    private final String value;

    EnumStudentCacheType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}