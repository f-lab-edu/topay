package com.topay.common.config.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @EnableRedisHttpSession 어노테이션과 RedisConnectionFactory 빈을 등록하여, Spring Session이 Redis와 연동되도록 구성
 * 이 설정에 의해, 애플리케이션의 HttpSession 관련 작업(생성, 업데이트, 조회, 삭제)이 Spring Session의 Redis 기반 구현으로 대체
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class SessionConfig {

    @Value("${spring.redis.session.host}")
    private String sessionRedisHost;

    @Value("${spring.redis.session.port}")
    private int sessionRedisPort;

    @Primary
    @Bean
    public RedisConnectionFactory sessionRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(sessionRedisHost, sessionRedisPort);

        /**
         * Redis와의 연결을 관리하며, 세션 데이터를 Redis에 저장하기 위해 사용
         */
        return new LettuceConnectionFactory(configuration);
    }

}
