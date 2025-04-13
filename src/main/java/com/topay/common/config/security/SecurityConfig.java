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

        // 1. 폼 로그인과 CSRF 비활성화 (REST API 형태)
        http.formLogin(form -> form.disable());
        http.csrf(csrf -> csrf.disable());

        // 2. 세션 정책 설정 (Redis Session 사용 시, 상태 기반 유지)
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        // 3. 엔드포인트 권한 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/logout", "/resources/**").permitAll()
                .anyRequest().authenticated()
        );

        // 4. 로그아웃 설정 (JSON 응답 기반)
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

        // 5. 커스텀 JSON 인증 필터 생성 및 설정
        JsonUsernamePasswordAuthenticationFilter jsonAuthFilter = new JsonUsernamePasswordAuthenticationFilter();
        jsonAuthFilter.setAuthenticationManager(authManager);
        jsonAuthFilter.setFilterProcessesUrl("/login");
        jsonAuthFilter.setAuthenticationSuccessHandler(new RestAuthenticationSuccessHandler());
        jsonAuthFilter.setAuthenticationFailureHandler(new RestAuthenticationFailureHandler());

        // 6. 기존 UsernamePasswordAuthenticationFilter 위치에 커스텀 필터 등록
        http.addFilterAt(jsonAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
