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

package com.ugrong.framework.redis.autoconfigure;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.ugrong.framework.redis.listener.MessageHandlerBeanLifecycleListener;
import com.ugrong.framework.redis.repository.cache.IStringRedisRepository;
import com.ugrong.framework.redis.repository.cache.impl.StringRedisRepositoryImpl;
import com.ugrong.framework.redis.repository.channel.IRedisChannelRepository;
import com.ugrong.framework.redis.repository.channel.impl.DefaultChannelRedisRepository;
import com.ugrong.framework.redis.repository.lock.IRedisLockRepository;
import com.ugrong.framework.redis.repository.lock.aop.RedisLockAspect;
import com.ugrong.framework.redis.repository.lock.impl.RedissonLockImpl;

@Configuration
@ConditionalOnClass({RedisOperations.class})
@AutoConfigureBefore({RedissonAutoConfiguration.class})
public class RedisAutoConfiguration {

	private static final String DEFAULT_DATA_PATTERN = "yyyy-MM-dd";

	private static final String DEFAULT_DATA_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	@Bean
	@ConditionalOnMissingBean
	public GenericJackson2JsonRedisSerializer jsonRedisSerializer() {
		ObjectMapper mapper = this.newMapper();
		//mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL, As.PROPERTY);
		return new GenericJackson2JsonRedisSerializer(mapper);
	}

	public ObjectMapper newMapper() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		//由于js的number只能表示15个数字，Long类型数字用String格式返回
		builder.serializerByType(Long.class, ToStringSerializer.instance);
		builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
		builder.serializerByType(long.class, ToStringSerializer.instance);

		//日期类型转换
		builder.simpleDateFormat(DEFAULT_DATA_TIME_PATTERN);

		//LocalDateTime按照 "yyyy-MM-dd HH:mm:ss"的格式进行序列化、反序列化
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATA_TIME_PATTERN);
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));

		//LocalDate按照 "yyyy-MM-dd"的格式进行序列化、反序列化
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATA_PATTERN);
		javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));

		//是否缩放排列输出，默认false
		// builder.indentOutput(true);
		builder.timeZone("Asia/Shanghai");

		builder.modules(
				//识别Java8时间
				new ParameterNamesModule(),
				new Jdk8Module(),
				javaTimeModule
		);
		builder.visibility(PropertyAccessor.ALL, Visibility.ANY);
		return builder.build();
	}


	@Bean
	//@ConditionalOnMissingBean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, GenericJackson2JsonRedisSerializer jsonRedisSerializer) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		//template.setDefaultSerializer(jacksonSerializer);

		// 设置值（value）的序列化采用GenericJackson2JsonRedisSerializer
		template.setValueSerializer(jsonRedisSerializer);
		template.setHashValueSerializer(jsonRedisSerializer);

		// 设置键（key）的序列化采用StringRedisSerializer
		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);

		template.afterPropertiesSet();
		return template;
	}

	@Bean
	@ConditionalOnBean(RedisTemplate.class)
	@ConditionalOnMissingBean
	public IStringRedisRepository stringRedisRepository() {
		return new StringRedisRepositoryImpl();
	}

	@Bean
	@ConditionalOnMissingBean
	public IRedisLockRepository redisLockRepository(RedissonClient redisson) {
		return new RedissonLockImpl(redisson);
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisLockAspect redisLockAspect(IRedisLockRepository redisLockRepository) {
		return new RedisLockAspect(redisLockRepository);
	}

	@Bean
	@ConditionalOnBean(RedisTemplate.class)
	@ConditionalOnMissingBean
	public IRedisChannelRepository redisChannelRepository(RedisTemplate<String, Object> redisTemplate) {
		return new DefaultChannelRedisRepository(redisTemplate);
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}

	@Bean
	@ConditionalOnMissingBean
	public MessageHandlerBeanLifecycleListener messageHandlerBeanLifecycleListener(RedisMessageListenerContainer container,
			GenericJackson2JsonRedisSerializer serializer) {
		return new MessageHandlerBeanLifecycleListener(container, serializer);
	}
}
