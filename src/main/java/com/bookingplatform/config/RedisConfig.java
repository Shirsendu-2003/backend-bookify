package com.bookingplatform.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(
            RedisConnectionFactory connectionFactory
    ) {

        RedisTemplate<String,Object> template =
                new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        RedisSerializer<Object> serializer =
                RedisSerializer.json();

        template.setKeySerializer(
                RedisSerializer.string()
        );

        template.setValueSerializer(
                serializer
        );

        template.setHashKeySerializer(
                RedisSerializer.string()
        );

        template.setHashValueSerializer(
                serializer
        );

        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory
    ) {

        RedisCacheConfiguration config =
                RedisCacheConfiguration
                        .defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(30))
                        .disableCachingNullValues()
                        .serializeValuesWith(
                                RedisSerializationContext
                                        .SerializationPair
                                        .fromSerializer(
                                                RedisSerializer.json()
                                        )
                        );

        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}