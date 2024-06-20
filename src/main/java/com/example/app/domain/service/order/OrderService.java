package com.example.app.domain.service.order;

import com.example.app.domain.dto.OrderDto;
import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.OrderRepository;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void savePayment(OrderDto orderDto){

        Project project = projectRepository.findByProCode(orderDto.getProCode());
        Optional<User> optionalUser = userRepository.findByUserId(orderDto.getUserId());

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Order order = Order.builder()
                    .proCode(project) //Project 엔티티에 추가됨
                    .userId(user)
                    .deliveryPay(orderDto.getDeliveryPay())
                    .orderMethod(orderDto.getOrderMethod())
                    .orderDate(orderDto.getOrderDate())
                    .orderState(orderDto.getOrderState())
                    .orderCnt(orderDto.getOrderCnt())
                    .refundDetail(orderDto.getRefundDetail())
                    .buyerName(orderDto.getBuyerName())
                    .buyerAddr(orderDto.getBuyerAddr())
//                    .buyerDetailAddr(orderDto.getBuyerDetailAddr())
                    .buyerTel(orderDto.getBuyerTel())
                    .buyerPostcode(orderDto.getBuyerPostcode())
                    .impUid(orderDto.getImpUid())
                    .merchantUid(orderDto.getMerchantUid())
                    .build();

            orderRepository.save(order);
        }else {
            throw new RuntimeException("USER not found");
        }
    }

}
