package com.example.app.domain.entity;

import com.example.app.domain.dto.UserDto;
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
@ToString
@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @Column(name = "userId", nullable = false)
    private String userId;
    @Column(name = "userPw", length = 60)
    private String userPw;
    @Column(name = "userName", nullable = false)
    private String userName;
    @Column(name = "phone", length = 13)
    private String phone;
    @Column(name = "role", nullable = false, length = 10)
    private String role;
    @Column(name = "userImg")
    private String userImg;
    @Column(name = "addrCode", length = 5)
    private String addrCode;
    @Column(name = "addrRoad")
    private String addrRoad;
    @Column(name = "addrDetail")
    private String addrDetail;
    @Column(name = "sns_type")
    private String snsType;
    @Column(name = "sns_connect_date")
    private String snsConnectDate;

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
                .snsType(userDto.getSnsType())
                .snsConnectDate(userDto.getSnsConnectDate())
                .build();
    }
}


