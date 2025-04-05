package com.topay.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    private String name;

    @Column(unique = true, updatable = false)
    private String email;

    private String password;

    @Embedded
    private Account account;

    private Level level;

    public static UserEntity of(String name, String email, Account account, Level level) {
        return new UserEntity(null, name, email, account, level);
    }

    public UserEntity(Long id, String name, String email, Account account, Level level) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.account = account;
        this.level = level;
    }

    // TODO: 유저 관련 비즈니스 행위를 본 도메인에서 구현한다.

}
