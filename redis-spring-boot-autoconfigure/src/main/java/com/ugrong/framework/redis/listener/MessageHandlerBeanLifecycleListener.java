/**
 * MIT License
 *
 * Copyright (c) 2019-2021 ugrong@163.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
