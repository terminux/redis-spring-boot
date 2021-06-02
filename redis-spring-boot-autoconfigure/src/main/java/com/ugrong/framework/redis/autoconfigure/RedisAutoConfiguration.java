package com.ugrong.framework.redis.autoconfigure;

import com.ugrong.framework.redis.listener.MessageHandlerBeanLifecycleListener;
import com.ugrong.framework.redis.repository.cache.IStringRedisRepository;
import com.ugrong.framework.redis.repository.cache.impl.StringRedisRepositoryImpl;
import com.ugrong.framework.redis.repository.channel.IRedisChannelRepository;
import com.ugrong.framework.redis.repository.channel.impl.DefaultChannelRedisRepository;
import com.ugrong.framework.redis.repository.lock.IRedisLockRepository;
import com.ugrong.framework.redis.repository.lock.aop.RedisLockAspect;
import com.ugrong.framework.redis.repository.lock.impl.RedissonLockImpl;
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

@Configuration
@ConditionalOnClass({RedisOperations.class})
@AutoConfigureBefore({RedissonAutoConfiguration.class})
public class RedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GenericJackson2JsonRedisSerializer jsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
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