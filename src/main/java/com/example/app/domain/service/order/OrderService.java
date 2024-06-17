package com.example.app.domain.service.order;

import com.example.app.domain.dto.OrderDto;
import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void savePayment(OrderDto orderDto){
        Order order = Order.builder()
                .proCode(new Project(orderDto.getProCode())) //Project 엔티티에 추가됨
                .userId(new User(orderDto.getUserId()))
                .deliveryPay(orderDto.getDeliveryPay())
                .orderMethod(orderDto.getOrderMethod())
                .orderDate(new Date())
                .orderState(orderDto.getOrderState())
                .orderCnt(orderDto.getOrderCnt())
                .refundDetail(orderDto.getRefundDetail())
                .orderName(orderDto.getOrderName())
                .orderAddr(orderDto.getOrderAddr())
                .orderPhone(orderDto.getOrderPhone())
                .postCode(orderDto.getPostCode())
                .build();

        orderRepository.save(order);
    }
}
