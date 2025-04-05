package com.topay.user.service;

import com.topay.common.utils.encrytion.EncryptionService;
import com.topay.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    // TODO: 회원 가입 기능

    // TODO: 이메일 중복 체크 기능

    // TODO: 사용자 조회 기능

    // TODO: 특정 사용자 검색 기능

    // TODO: ...

}
