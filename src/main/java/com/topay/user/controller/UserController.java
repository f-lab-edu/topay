package com.topay.user.controller;

import com.topay.common.annotation.CurrentUser;
import com.topay.common.annotation.LoginCheck;
import com.topay.user.controller.dto.request.LoginRequest;
import com.topay.user.service.SessionService;
import com.topay.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    // TODO: 회원 가입 API 엔드포인트

    // TODO: 이메일 중복 체크 API 엔드포인트

    // TODO: 사용자 조회 API 엔드포인트

    // TODO: 특정 사용자 검색 API 엔드포인트

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        sessionService.login(loginRequest);
    }

    @LoginCheck
    @DeleteMapping("/logout")
    public void logout(@CurrentUser String email) {
        sessionService.logout();
    }

}
