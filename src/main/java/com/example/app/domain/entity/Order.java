package com.example.app.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int orderCode;
    @ManyToOne
    @JoinColumn(name = "proCode", foreignKey = @ForeignKey(name = "FK_ORDER_PROJECT",
            foreignKeyDefinition = "FOREIGN KEY(proCode) REFERENCES tbl_project(proCode) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Project proCode;
    @ManyToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "FK_ORDER_USER",
            foreignKeyDefinition = "FOREIGN KEY(userId) REFERENCES tbl_user(userId) ON DELETE CASCADE ON UPDATE CASCADE"))
    private User userId;
    @Column(nullable = false, columnDefinition = "integer default 3000")
    private int deliveryPay;
    @Column(nullable = false)
    private String orderMethod;
    @Column(nullable = false)
    private Date orderDate;
    @Column(nullable = false)
    private String orderState;
    @Column(nullable = false)
    private int orderCnt;
    private String refundDetail;

    //추가
    @Column(nullable = false)
    private String orderName;
    private String orderAddr;
    @Column(nullable = false)
    private String orderPhone;
    @Column(nullable = false)
    private int postCode;
}
