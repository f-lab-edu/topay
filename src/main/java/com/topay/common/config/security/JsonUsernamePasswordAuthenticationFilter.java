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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // JSON 형태의 요청만 처리
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(request.getContentType())) {

            try {
                LoginRequestDto loginDto = objectMapper.readValue(request.getReader(), LoginRequestDto.class);
                String username = loginDto.getEmail();
                String password = loginDto.getPassword();

                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(username, password);

                setDetails(request, authRequest);

                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                throw new AuthenticationServiceException("JSON 파싱 중 오류 발생", e);
            }
        }

        // 만약 JSON이 아닌 경우, 폼 방식 활용
        return super.attemptAuthentication(request, response);
    }

}