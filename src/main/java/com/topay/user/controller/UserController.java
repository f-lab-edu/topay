package com.topay.user.controller;

import com.topay.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    // TODO: 회원 가입 API 엔드포인트

    // TODO: 이메일 중복 체크 API 엔드포인트

    // TODO: 사용자 조회 API 엔드포인트

    // TODO: 특정 사용자 검색 API 엔드포인트

    // TODO: 로그인 API 엔드포인트

    // TODO: 로그아웃 API 엔드포인트

}
