package com.ugrong.framework.redis.repository.channel.impl;

import com.ugrong.framework.redis.domain.IRedisTopicType;
import com.ugrong.framework.redis.repository.AbstractRedisRepository;
import com.ugrong.framework.redis.repository.channel.IRedisChannelRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

public class DefaultChannelRedisRepository extends AbstractRedisRepository<IRedisTopicType> implements IRedisChannelRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String NOTIFY_PAYLOAD = "redis message notify.";

    public DefaultChannelRedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getKeyDelimiter() {
        return IRedisChannelRepository.TOPIC_REDIS_KEY_DELIMITER;
    }

    @Override
    public void notify(IRedisTopicType topicType) {
        this.notify(topicType, null);
    }

    @Override
    public void notify(IRedisTopicType topicType, String topicSuffix) {
        this.publish(topicType, topicSuffix, NOTIFY_PAYLOAD);
    }

    @Override
    public void publish(IRedisTopicType topicType, Object payload) {
        this.publish(topicType, null, payload);
    }

    @Override
    public void publish(IRedisTopicType topicType, String topicSuffix, Object payload) {
        Assert.notNull(payload, "This payload is required; it must not be null");
        redisTemplate.convertAndSend(super.getKey(topicType, topicSuffix), payload);
    }
}
