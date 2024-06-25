package com.example.app.domain.repository;

import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    // 주문코드별 주문 조회
    Optional<Order> findByImpUid(String impUid);

    // 사용자별 + 주문상태별 주문 횟수 조회
    Long countByUserIdAndOrderState(User userId, String orderState);

    // 사용자별 + 주문상태별 주문 조회
    List<Order> findByUserIdAndOrderState(User userId, String orderState);
}
