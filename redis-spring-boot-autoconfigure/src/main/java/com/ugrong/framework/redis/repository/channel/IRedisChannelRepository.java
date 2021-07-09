package com.ugrong.framework.redis.repository.channel;


import com.ugrong.framework.redis.domain.IRedisTopicType;
import com.ugrong.framework.redis.repository.IRedisRepository;

/**
 * redis实现发布消息.
 */
public interface IRedisChannelRepository extends IRedisRepository {

	String TOPIC_REDIS_KEY_DELIMITER = "/";

	/**
	 * 发送一个通知，使用空的payload.
	 *
	 * @param topicType 通知主题类型
	 */
	void notify(IRedisTopicType topicType);

	/**
	 * 发送一个通知，使用空的payload.
	 *
	 * @param topicType   通知主题类型
	 * @param topicSuffix 通知主题后缀
	 */
	void notify(IRedisTopicType topicType, String topicSuffix);

	/**
	 * 发布消息.
	 *
	 * @param topicType the topic type
	 * @param payload   消息载体
	 */
	void publish(IRedisTopicType topicType, Object payload);

	/**
	 * 发布消息.
	 *
	 * @param topicType   the topic type
	 * @param topicSuffix 消息主题后缀
	 * @param payload     消息载体
	 */
	void publish(IRedisTopicType topicType, String topicSuffix, Object payload);
}
