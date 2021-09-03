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
