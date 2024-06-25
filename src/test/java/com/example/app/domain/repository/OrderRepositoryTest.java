package com.example.app.domain.repository;

import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void findById() {
        User user = new User();
        Optional<User> optionalUser = userRepository.findById("hjs971102@naver.com");
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        System.out.println("user = " + user);
    }

    @Test
    public void findByUserIdAndOrderState() {
        User user = new User();
        Optional<User> optionalUser = userRepository.findById("hjs971102@naver.com");
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        System.out.println("user = " + user);
        List<Order> orderList = orderRepository.findByUserIdAndOrderState(user, "결제완료");
        orderList.forEach(entity -> {
            System.out.println("entity = " + entity);
        });
    }

}