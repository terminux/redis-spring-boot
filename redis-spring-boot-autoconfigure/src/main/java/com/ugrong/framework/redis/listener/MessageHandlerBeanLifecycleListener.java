package com.ugrong.framework.redis.listener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import com.ugrong.framework.redis.annotation.RedisHandler;
import com.ugrong.framework.redis.domain.IRedisTopicType;
import com.ugrong.framework.redis.handler.IRedisMessageHandler;
import com.ugrong.framework.redis.repository.channel.IRedisChannelRepository;
import com.ugrong.framework.redis.utils.ProxyUtil;
import com.ugrong.framework.redis.utils.RedisKeyUtil;

/**
 * message handler bean 生命周期监听器
 */
public class MessageHandlerBeanLifecycleListener implements BeanPostProcessor {

	private final RedisMessageListenerContainer container;

	private final GenericJackson2JsonRedisSerializer serializer;

	private static final String LISTENER_METHOD = "handle";

	public MessageHandlerBeanLifecycleListener(RedisMessageListenerContainer container, GenericJackson2JsonRedisSerializer serializer) {
		this.container = container;
		this.serializer = serializer;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (IRedisMessageHandler.class.isAssignableFrom(bean.getClass())) {
			IRedisMessageHandler<?> handler = (IRedisMessageHandler<?>) bean;
			Class<?> handlerClass = ProxyUtil.getOriginClass(handler);
			if (handlerClass != null) {
				RedisHandler redisHandler = handlerClass.getAnnotation(RedisHandler.class);
				if (redisHandler != null && StringUtils.isNotBlank(redisHandler.topic())) {
					this.addMessageListener(handler, redisHandler.topic());
				}
			}
		}
		return bean;
	}

	private void addMessageListener(IRedisMessageHandler<?> handler, String topic) {
		String fullTopic = RedisKeyUtil.concatKey(IRedisTopicType.DEFAULT_REDIS_TOPIC_TYPE,
				IRedisChannelRepository.TOPIC_REDIS_KEY_DELIMITER, topic);
		container.addMessageListener(this.newListener(handler), new PatternTopic(fullTopic));
	}

	public MessageListenerAdapter newListener(IRedisMessageHandler<?> handler) {
		MessageListenerAdapter adapter = new MessageListenerAdapter(handler, LISTENER_METHOD);
		adapter.setSerializer(serializer);
		adapter.afterPropertiesSet();
		return adapter;
	}
}
