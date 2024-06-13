package com.example.app.domain.service.myPage;

import com.example.app.domain.entity.User;
import com.example.app.domain.repository.NotifyRepository;
import com.example.app.domain.repository.OrderRepository;
import com.example.app.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BuyerServiceImpl {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    NotifyRepository notifyRepository;

    @Autowired
    UserRepository userRepository;


    public Long CountNotifyByUserId(String userId){
        Long cntNotify = notifyRepository.CountByUserId(userId);
        return cntNotify;
    }

    public Long CountOrderByUserId(String userId){
        Long cntOrder = orderRepository.CountByUserId(userId);
        return cntOrder;
    }

    public User findUserByUserId(String userId){
        Optional<User> findUserName = userRepository.findByUserId(userId);

        return findUserName.orElse(null);
    }
}
