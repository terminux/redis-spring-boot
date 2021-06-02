package com.ugrong.framework.redis.repository.channel.impl;

import com.ugrong.framework.redis.domain.IRedisTopicType;
import com.ugrong.framework.redis.repository.AbstractRedisRepository;
import com.ugrong.framework.redis.repository.channel.IRedisChannelRepository;
import org.springframework.data.redis.core.RedisTemplate;

public class DefaultChannelRedisRepository extends AbstractRedisRepository<IRedisTopicType> implements IRedisChannelRepository {

    private final RedisTemplate<String, Object> redisTemplate;

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
        this.publish(topicType, topicSuffix, null);
    }

    @Override
    public void publish(IRedisTopicType topicType, Object payload) {
        this.publish(topicType, null, payload);
    }

    @Override
    public void publish(IRedisTopicType topicType, String topicSuffix, Object payload) {
        redisTemplate.convertAndSend(super.getKey(topicType, topicSuffix), payload);
    }
}
