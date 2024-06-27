package com.example.app.domain.repository;

import com.example.app.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    // userId 별 user 정보 조회
    Optional<User> findByUserId(String userId);

    // userId 별 user 정보 삭제
    void deleteByUserId(String userId);

    // 모든 user 정보 조회 (페이지 처리)
    Page<User> findAll(Pageable pageable);

    // user 역할 별 user 정보 조회
    Optional<User> findByRole(String role);

}
