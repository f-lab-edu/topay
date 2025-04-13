package com.topay.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    private String userName;

    @Column(unique = true, updatable = false)
    private String email;

    private String password;

    @Embedded
    private Account account;

    private Level level;

    public static UserEntity of(String userName, String email, String password, Account account, Level level) {
        return UserEntity.builder()
                .userName(userName)
                .email(email)
                .password(password)
                .account(account)
                .level(level)
                .build();
    }

    @Builder
    public UserEntity(Long id, String userName, String email, String password, Account account, Level level) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.account = account;
        this.level = level;
    }

    // TODO: 유저 관련 비즈니스 행위를 본 도메인에서 구현한다.

}
