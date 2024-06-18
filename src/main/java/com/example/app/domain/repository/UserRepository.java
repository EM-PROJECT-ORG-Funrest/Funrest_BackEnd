package com.example.app.domain.repository;

import com.example.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);

    @Override
    List<User> findAll();

    Optional<User> findByRole(String role);
}
