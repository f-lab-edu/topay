package com.topay.user.service;

import com.topay.user.controller.dto.request.LoginRequest;
import com.topay.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SessionService {
    public static final String USER_ID = "email";

    private final HttpSession session;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public void login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();

        // TODO: 예외 처리 및 추가 구현 필요

        session.setAttribute(USER_ID, email);
    }

    public void logout() {
        session.removeAttribute(USER_ID);
    }

}
