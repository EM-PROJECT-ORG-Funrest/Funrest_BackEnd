package com.example.app.domain.entity;

import com.example.app.domain.dto.OrderDto;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.weaver.ast.Or;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_order")
public class Order {
    @Id
    @Column(nullable = false, name = "orderCode")
    private String impUid;
    @ManyToOne
    @JoinColumn(name = "proCode", foreignKey = @ForeignKey(name = "FK_ORDER_PROJECT",
            foreignKeyDefinition = "FOREIGN KEY(proCode) REFERENCES tbl_project(proCode) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Project proCode;
    @ManyToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "FK_ORDER_USER",
            foreignKeyDefinition = "FOREIGN KEY(userId) REFERENCES tbl_user(userId) ON DELETE CASCADE ON UPDATE CASCADE"))
    private User userId;
    private int deliveryPay;
    @Column(nullable = false)
    private String orderMethod;
    @Column(nullable = false)
    private LocalDate orderDate;
    @Column(nullable = false)
    private String orderState;
    @Column(nullable = false)
    private int orderCnt;
    private String refundDetail;
    private int refundCnt;
    @Column(nullable = false)
    private String buyerName;
    private String buyerAddr;
    @Column(nullable = false)
    private String buyerTel;
    @Column(nullable = false)
    private int buyerPostcode;
    @Column(nullable = false)
    private int totalAmount;
    @Column(nullable = false)
    private String merchantUid;

    // Dto to Entity
    public static Order toEntity(OrderDto orderDto) {
        return Order.builder()
                .proCode(new Project(orderDto.getProCode()))
                .userId(new User(orderDto.getUserId()))
                .deliveryPay(orderDto.getDeliveryPay())
                .orderMethod(orderDto.getOrderMethod())
                .orderDate(orderDto.getOrderDate())
                .orderState(orderDto.getOrderState())
                .orderCnt(orderDto.getOrderCnt())
                .refundDetail(orderDto.getRefundDetail())
                .refundCnt(orderDto.getRefundCnt())
                .buyerName(orderDto.getBuyerName())
                .buyerAddr(orderDto.getBuyerAddr())
                .buyerTel(orderDto.getBuyerTel())
                .buyerPostcode(orderDto.getBuyerPostcode())
                .impUid(orderDto.getImpUid())
                .merchantUid(orderDto.getMerchantUid())
                .totalAmount(orderDto.getTotalAmount())
                .build();
    }
}