package com.example.app.domain.repository;

import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Long countByUserIdAndOrderState(User userId, String orderState);

    Optional<Order> findByImpUid(String impUid);

    List<Order> findByUserId(User userId);

    List<Order> findByUserIdAndOrderState(User userId, String orderState);
}
