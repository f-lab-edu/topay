package com.topay.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    // JSON 파싱에 사용할 ObjectMapper 인스턴스
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", "로그인 실패");
        responseData.put("error", exception.getMessage());

        // Map을 JSON 문자열로 변환하여 클라이언트로 응답 전송
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

}