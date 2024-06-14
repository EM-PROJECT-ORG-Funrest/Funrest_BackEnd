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


    //사용자별 알림 신청 횟수 찾기
    public Long countNotifyByUserId(User user){
        return notifyRepository.countByUserId(user);
    }
    
    //사용자별 주문 신청 횟수 찾기
    public Long countOrderByUserId(User user){
        return orderRepository.countByUserId(user);
    }

    //사용자Id 로 사용자 정보 찾기
    public User findUserByUserId(String userId){
        Optional<User> findUserName = userRepository.findByUserId(userId);

        return findUserName.orElse(null);
    }
}
