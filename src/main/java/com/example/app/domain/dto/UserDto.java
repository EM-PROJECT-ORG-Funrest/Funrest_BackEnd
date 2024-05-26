package com.example.app.domain.dto;

import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
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

    public static UserDto EntityToUserDto(User user){

        return UserDto.builder()
                .userId(user.getUserId())
                .userPw(user.getUserPw())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .role(user.getRole())
                .userImg(user.getUserImg())
                .addrCode(user.getAddrCode())
                .addrRoad(user.getAddrRoad())
                .addrDetail(user.getAddrDetail())
                .build();
    }
}
