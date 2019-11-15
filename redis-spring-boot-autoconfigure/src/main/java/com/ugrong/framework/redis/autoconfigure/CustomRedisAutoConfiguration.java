package com.ugrong.framework.redis.autoconfigure;

import com.ugrong.framework.redis.aop.RedisLockAspect;
import com.ugrong.framework.redis.lock.RedisLockScript;
import com.ugrong.framework.redis.lock.service.RedisLockService;
import com.ugrong.framework.redis.lock.service.impl.RedisLockServiceImpl;
import com.ugrong.framework.redis.repository.impl.LockRedisRepositoryImpl;
import com.ugrong.framework.redis.repository.impl.StringRedisRepositoryImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnClass({RedisOperations.class})
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class CustomRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jacksonSerializer = new GenericJackson2JsonRedisSerializer();

        // 设置值（value）的序列化采用GenericJackson2JsonRedisSerializer
        template.setValueSerializer(jacksonSerializer);
        template.setHashValueSerializer(jacksonSerializer);

        // 设置键（key）的序列化采用StringRedisSerializer
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisLockScript redisLockScript() {
        return new RedisLockScript();
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    @ConditionalOnMissingBean
    public StringRedisRepositoryImpl stringRedisRepository() {
        return new StringRedisRepositoryImpl();
    }

    @Bean
    @ConditionalOnBean(RedisLockScript.class)
    @ConditionalOnMissingBean(value = LockRedisRepositoryImpl.class, ignored = StringRedisRepositoryImpl.class)
    public LockRedisRepositoryImpl lockRedisRepository() {
        return new LockRedisRepositoryImpl();
    }

    @Bean
    @ConditionalOnBean(LockRedisRepositoryImpl.class)
    @ConditionalOnMissingBean
    public RedisLockService redisLockService() {
        return new RedisLockServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisLockService.class)
    public RedisLockAspect redisLockAspect() {
        return new RedisLockAspect();
    }
}