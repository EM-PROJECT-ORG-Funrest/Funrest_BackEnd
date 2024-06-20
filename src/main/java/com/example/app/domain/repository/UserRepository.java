package com.example.app.domain.repository;

import com.example.app.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);


    void deleteByUserId(String userId);

    Page<User> findAll(Pageable pageable);

    Optional<User> findByRole(String role);
}
