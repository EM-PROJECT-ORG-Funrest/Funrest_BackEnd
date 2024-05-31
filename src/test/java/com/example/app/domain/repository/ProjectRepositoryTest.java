package com.example.app.domain.repository;

import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;


    public User user;

    @Test
    public void t1(){
        assertNotNull(projectRepository);
    }

    @Test
    public void t2(){
        User user = User.builder()
                .userId("20")
                .userPw("1234")
                .userName("hong")
                .role("seller")
                .build();
        this.user = user;
        userRepository.save(user);
    }


//    @Test
//    public void insert(){
//
////         User user1 = User.builder()
////                .userId("12")
////                .build();
//        Project project = Project.builder()
//                .proCode(1203)
//                .userId(user)
//                .proCategory("영화")
//                .proName("트랜스포머3")
//                .proImg("movie.img")
//                .proMainImg("movieMain.img")
//                .proPrice(1200)
//                .proDate(new Date())
//                .proStartDate(new Date())
//                .proEndDate(new Date())
//                .proStatus("승인")
//                .proPaidCnt(1)
//                .proNotifyCnt(2)
//                .proScript("영화입니다")
//                .sellerName("홍길동")
//                .sellerDetail("영화판매자입니다")
//                .build();
//
//        projectRepository.save(project);
//    }
}