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

package com.ugrong.framework.redis.repository.channel.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import com.ugrong.framework.redis.domain.IRedisTopicType;
import com.ugrong.framework.redis.repository.AbstractRedisRepository;
import com.ugrong.framework.redis.repository.channel.IRedisChannelRepository;

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
