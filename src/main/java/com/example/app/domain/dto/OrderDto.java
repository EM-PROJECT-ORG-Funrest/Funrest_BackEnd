package com.example.app.domain.dto;

import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private String impUid;
    private int proCode;
    private String userId;
    private int deliveryPay;
    private String orderMethod;
    private LocalDate orderDate;
    private String orderState;
    private int orderCnt;
    private String refundDetail;
    private int refundCnt;
    private String buyerName;
    private String buyerAddr;
    //private String buyerDetailAddr;
    private String buyerTel;
    private int buyerPostcode;
    private int totalAmount;
    private String merchantUid;

    // Entity to Dto
    public static OrderDto EntityToOrderDto(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setImpUid(order.getImpUid());
        orderDto.setProCode(order.getProCode().getProCode());
        orderDto.setUserId(order.getUserId().getUserId());
        orderDto.setDeliveryPay(order.getDeliveryPay());
        orderDto.setOrderMethod(order.getOrderMethod());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setOrderState(order.getOrderState());
        orderDto.setOrderCnt(order.getOrderCnt());
        orderDto.setRefundDetail(order.getRefundDetail());
        orderDto.setRefundCnt(order.getRefundCnt());
        orderDto.setBuyerName(order.getBuyerName());
        orderDto.setBuyerAddr(order.getBuyerAddr());
        //orderDto.setBuyerDetailAddr(order.getBuyerDetailAddr());
        orderDto.setBuyerTel(order.getBuyerTel());
        orderDto.setBuyerPostcode(order.getBuyerPostcode());
        orderDto.setMerchantUid(order.getMerchantUid());
        orderDto.setTotalAmount(order.getTotalAmount());
        return orderDto;
    }
}