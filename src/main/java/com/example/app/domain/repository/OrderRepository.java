package com.example.app.domain.repository;

import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    // 주문코드별 주문 조회
    Optional<Order> findByImpUid(String impUid);

    // 사용자별 + 주문상태별 주문 횟수 조회
    Long countByUserIdAndOrderState(User userId, String orderState);

    // 사용자별 + 주문상태별 주문 조회
    List<Order> findByUserIdAndOrderStateOrderByOrderDateDesc(User userId, String orderState);

    // 프로젝트별 주문횟수 조회
    Long countByProCode(Project proCode);

    // 모든 주문 정보 조회 (페이지 처리)
    Page<Order> findAll(Pageable pageable);

    // 주문 일자를 내림차순으로 정렬하여 각 날짜별 주문 수, 총 결제금액 조회
    @Query("SELECT o.orderDate, SUM(o.orderCnt), SUM(o.totalAmount) " +
            "FROM Order o " +
            "GROUP BY o.orderDate " +
            "ORDER BY o.orderDate DESC")
    Page<Object[]> findOrderStatsByOrderDate(Pageable pageable);
}
