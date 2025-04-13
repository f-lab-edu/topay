package com.topay.common.config.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

        /**
         * REST API로 사용하기 위해, 기본 폼 로그인과 CSRF 보호 기능을 비활성
         */
        http.formLogin(form -> form.disable());
        http.csrf(csrf -> csrf.disable());

        /**
         * 요청마다(혹은 세션이 없는 요청마다) 새로운 HTTP 세션을 생성
         */
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        /**
         * "엔드포인트 권한 설정"
         *
         * /login, /logout, /resources/** 경로에 대해서는 인증 없이 접근할 수 있도록 하고,
         * 그 외의 요청은 인증된 사용자만 접근할 수 있도록 설정
         */
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/logout", "/resources/**").permitAll()
                .anyRequest().authenticated()
        );

        /**
         * 로그아웃 URL(/logout)에 접근 시,
         * 세션 무효화와 쿠키 삭제(JSESSIONID) 후 JSON 형태의 응답을 반환하도록 설정
         */
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write("{\"message\": \"로그아웃 성공\"}");
                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        );

        /**
         * "커스텀 JSON 인증 필터 등록"
         *
         * 기존의 폼 기반 로그인 대신, JSON 형식의 로그인 요청을 처리하기 위해 커스텀 필터
         * JSON 요청을 파싱하여 UsernamePasswordAuthenticationToken을 생성하고, AuthenticationManager를 통해 인증을 수행
         */
        JsonUsernamePasswordAuthenticationFilter jsonAuthFilter = new JsonUsernamePasswordAuthenticationFilter();
        jsonAuthFilter.setAuthenticationManager(authManager);
        jsonAuthFilter.setFilterProcessesUrl("/login");
        jsonAuthFilter.setAuthenticationSuccessHandler(new RestAuthenticationSuccessHandler());
        jsonAuthFilter.setAuthenticationFailureHandler(new RestAuthenticationFailureHandler());

        /**
         * 기존 UsernamePasswordAuthenticationFilter 위치에 커스텀 필터 등록
         */
        http.addFilterAt(jsonAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
