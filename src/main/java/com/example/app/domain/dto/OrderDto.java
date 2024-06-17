package com.example.app.domain.dto;

import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.Project;
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
    private int postCode;

    public static OrderDto EntityToOrderDto(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderCode(order.getOrderCode());
        orderDto.setProCode(order.getProCode().getProCode());
        orderDto.setUserId(order.getUserId().getUserId());
        orderDto.setDeliveryPay(order.getDeliveryPay());
        orderDto.setOrderMethod(order.getOrderMethod());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setOrderState(order.getOrderState());
        orderDto.setOrderCnt(order.getOrderCnt());
        orderDto.setRefundDetail(order.getRefundDetail());
        orderDto.setOrderName(order.getOrderName());
        orderDto.setOrderAddr(order.getOrderAddr());
        orderDto.setOrderPhone(order.getOrderPhone());
        orderDto.setPostCode(order.getPostCode());

        return orderDto;
    }
}
