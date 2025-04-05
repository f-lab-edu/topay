package com.topay.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserEntityTest {

    private String name;
    private String email;
    private Account account;
    private Level level;

    @BeforeEach
    void setUp() {
        // given
        name = "Hong Jung Wan";
        email = "jungwan.hong@example.com";
        account = new Account("MyBank", "1234567890", "Hong Jung Wan");
        level = Level.UN_AUTH;
    }

    @Test
    @DisplayName("UserEntity 객체 생성 테스트")
    void testUserEntityOf() {
        // when
        UserEntity user = UserEntity.of(name, email, account, level);

        // then
        assertNull(user.getId(), "새로 생성된 UserEntity의 id는 null이어야 한다.");
        assertEquals(name, user.getName(), "이름이 일치해야 한다.");
        assertEquals(email, user.getEmail(), "이메일이 일치해야 한다.");
        assertEquals(account, user.getAccount(), "계좌 정보가 일치해야 한다.");
        assertEquals(level, user.getLevel(), "레벨이 일치해야 한다.");
    }

}
