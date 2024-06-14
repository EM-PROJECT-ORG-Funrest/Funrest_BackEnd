package com.example.app.domain.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private int orderCode;
    private int proCode;
    private String userId;
    private int deliveryPay;
    private String orderMethod;
    private Date orderDate;
    private String orderState;
    private int orderCnt;
    private String refundDetail;
    
    //추가
    private String orderName;
    private String orderAddr;
    private String orderPhone;
    private String postCode;
}
