package com.topay.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class SessionConfig {

    @Value("${spring.redis.session.host}")
    private String sessionRedisHost;

    @Value("${spring.redis.session.port}")
    private int sessionRedisPort;

    @Primary
    @Bean
    public RedisConnectionFactory sessionRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(sessionRedisHost, sessionRedisPort);
        return new LettuceConnectionFactory(configuration);
    }

}
