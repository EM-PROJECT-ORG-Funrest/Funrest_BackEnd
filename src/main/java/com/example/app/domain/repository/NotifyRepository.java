package com.example.app.domain.repository;

import com.example.app.domain.entity.Notify;
import com.example.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<Notify, Integer> {
    Long countByUserId(User user);
}
