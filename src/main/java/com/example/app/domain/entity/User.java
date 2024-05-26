package com.example.app.domain.entity;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.dto.UserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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

    public static User UserDtoToEntity(UserDto userDto) {
        return User.builder()
                .userId(userDto.getUserId())
                .userPw(userDto.getUserPw())
                .userName(userDto.getUserName())
                .phone(userDto.getPhone())
                .role(userDto.getRole())
                .userImg(userDto.getUserImg())
                .addrCode(userDto.getAddrCode())
                .addrRoad(userDto.getAddrRoad())
                .addrDetail(userDto.getAddrDetail())
                .build();
    }
}
