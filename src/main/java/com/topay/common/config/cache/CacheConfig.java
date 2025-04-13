package com.topay.common.config.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.redis.cache.host}")
    private String cacheRedisHost;

    @Value("${spring.redis.cache.port}")
    private int cacheRedisPort;

    @Bean
    public LettuceConnectionFactory cacheRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(cacheRedisHost, cacheRedisPort);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory cacheRedisConnectionFactory) {
        return RedisCacheManager.builder(cacheRedisConnectionFactory).build();
    }

}
