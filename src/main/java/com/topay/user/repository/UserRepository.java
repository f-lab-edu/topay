package com.topay.user.repository;

import com.topay.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // TODO: 사용자 도메인에 특화된 커스텀 쿼리 메소드 추가 (예: 이메일로 사용자 조회, 사용자명 검색 등)

}
