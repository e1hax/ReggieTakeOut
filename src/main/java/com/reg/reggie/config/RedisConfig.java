package com.reg.reggie.config;

import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by e1hax on 2022-09-15.
 */
@Configuration
public class RedisConfig extends CachingConfigurationSelector {
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory connectionFactory){

        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        //默认的key序列化器为：JdkSerializationRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //默认序列化器：对所有生效
        //redisTemplate.setDefaultSerializer(new StringRedisSerializer());

        redisTemplate.setConnectionFactory(connectionFactory);

        return redisTemplate;
    }
}
