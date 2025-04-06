package com.topay.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 로그인 여부 검사, 권한 체크 수행 -> 컨트롤러 호출 전/후 or 응답 생성 후 동작
 */
@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    // TODO: 구현 예정
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // TODO: test 환경에서는 인터셉트가 동작하지 않도록 설정

        return true;
    }

    // TODO: 구현 예정
    private void authUserLevel() {

    }

    // TODO: 구현 예정
    private void adminUserLevel() {

    }

}
