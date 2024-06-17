package com.example.app.domain.repository;

import com.example.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);


    void deleteByUserId(String userId);
}
