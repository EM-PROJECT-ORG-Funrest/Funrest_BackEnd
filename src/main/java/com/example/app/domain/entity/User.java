package com.example.app.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @Column(name = "userId", nullable = false)
    private String userId;
    @Column(name = "userPw", nullable = false)
    private String userPw;
    @Column(name = "userName", nullable = false)
    private String userName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "role", nullable = false)
    private String role;
    @Column(name = "userImg")
    private String userImg;
    @Column(name = "addrCode")
    private String addrCode;
    @Column(name = "addrRoad")
    private String addrRoad;
    @Column(name = "addrDetail")
    private String addrDetail;

    //카카오
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 자동 생성
//    private Long id;
//
//    @Column(name = "kakaoId")
//    private Long kakaoId;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "nickname")
//    private String nickname;
//
//    @Column(name = "accessToken")
//    private String accessToken;
//
//    //refreshToken은 할지 말지?
//    @Column(name = "refreshToken")
//    private String refreshToken;

}


