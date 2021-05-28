package com.ugrong.framework.redis.samples.repository;

import com.ugrong.framework.redis.domain.IRedisCacheType;
import com.ugrong.framework.redis.repository.cache.impl.AbstractSimpleRedisRepository;
import com.ugrong.framework.redis.samples.model.Student;
import com.ugrong.framework.redis.samples.type.EnumStudentCacheType;
import org.springframework.stereotype.Component;

@Component
public class StudentRedisRepository extends AbstractSimpleRedisRepository<Student> {

    @Override
    public IRedisCacheType getCacheType() {
        return EnumStudentCacheType.STUDENT_CACHE;
    }
}