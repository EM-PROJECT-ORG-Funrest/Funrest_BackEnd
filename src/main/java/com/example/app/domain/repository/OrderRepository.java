package com.example.app.domain.repository;

import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Long countByUserId(User user);

    Optional<Order> findByImpUid(String impUid);
}
