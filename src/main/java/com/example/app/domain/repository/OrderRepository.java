package com.example.app.domain.repository;

import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Long countByUserId(User user);
}
