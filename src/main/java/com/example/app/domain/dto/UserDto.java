package com.example.app.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;
    private String userPw;
    private String userName;
    private String phone;
    private String role;
    private String userImg; // 유저 이미지
    private String addrCode; // 우편번호
    private String addrRoad;
    private String addrDetail;
}
