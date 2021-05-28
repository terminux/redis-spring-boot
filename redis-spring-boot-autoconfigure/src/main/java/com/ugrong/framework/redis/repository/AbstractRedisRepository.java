package com.ugrong.framework.redis.repository;

import com.ugrong.framework.redis.domain.IRedisType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

public abstract class AbstractRedisRepository<T extends IRedisType> implements IRedisRepository {

    protected void validTimeArgs(long timeout, TimeUnit timeUnit) {
        Assert.isTrue(timeout > 0 && timeUnit != null, "This [timeout] and [timeUnit] is required; it must not be null");
    }

    protected final String getKey(T type, String keySuffix) {
        Assert.isTrue(type != null && StringUtils.isNotBlank(type.getType()), "This redis type is required; it must not be null");
        //Assert.notBlank(keySuffix, "This redis key suffix is required; it must not be null");
        String prefix = type.getType().concat(REDIS_KEY_DELIMITER);
        if (StringUtils.isBlank(keySuffix)) {
            return prefix.concat(this.getTypeValue(type));
        }
        return prefix.concat(this.getKeyPrefix(type)).concat(keySuffix);
    }

    protected final String getKeyPrefix(T type) {
        return this.getTypeValue(type).concat(REDIS_KEY_DELIMITER);
    }

    protected final String getTypeValue(T type) {
        Assert.isTrue(type != null && StringUtils.isNotBlank(type.getType()), "This redis type is required; it must not be null");
        return type.getValue();
    }
}
