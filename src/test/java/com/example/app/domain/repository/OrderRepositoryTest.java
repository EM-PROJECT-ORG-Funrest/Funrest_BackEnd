package com.example.app.domain.repository;

import com.example.app.domain.entity.Order;
import com.example.app.domain.entity.Project;
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

    @Autowired
    private ProjectRepository projectRepository;

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
    public void findByUserIdAndOrderStateOrderByOrderDateDesc() {
        User user = new User();
        Optional<User> optionalUser = userRepository.findById("hjs971102@naver.com");
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        System.out.println("user = " + user);
        List<Order> orderList = orderRepository.findByUserIdAndOrderStateOrderByOrderDateDesc(user, "결제완료");
        orderList.forEach(entity -> {
            System.out.println("entity = " + entity);
        });
    }

    @Test
    public void countByProCode() {
        Optional<Project> optionalProject = projectRepository.findById(7);
        Project project = new Project();
        if (optionalProject.isPresent()) {
            project = optionalProject.get();
        }
        Long proPaidCnt = orderRepository.countByProCode(project);
        System.out.println("proPaidCnt = " + proPaidCnt);
    }

}