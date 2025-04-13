package com.topay.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.topay.user.controller.dto.request.LoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // JSON 파싱에 사용할 ObjectMapper 인스턴스
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 요청의 Content-Type이 JSON인지 확인
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(request.getContentType())) {

            try {
                return extractAndAuthenticateFromJson(request);
            } catch (IOException e) {
                throw new AuthenticationServiceException("JSON 파싱 중 오류 발생", e);
            }
        }

        // 요청이 JSON 형식이 아닌 경우에는 기본 폼 로그인 방식으로 처리
        return super.attemptAuthentication(request, response);
    }

    private Authentication extractAndAuthenticateFromJson(HttpServletRequest request) throws IOException {
        // 요청 본문에서 JSON 데이터를 파싱하여 LoginRequestDto 객체로 변환
        LoginRequestDto loginDto = objectMapper.readValue(request.getReader(), LoginRequestDto.class);

        String username = loginDto.getEmail();
        String password = loginDto.getPassword();

        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);

        setDetails(request, authRequest);

        // AuthenticationManager를 통해 실제 인증을 수행
        return this.getAuthenticationManager().authenticate(authRequest);
    }

}