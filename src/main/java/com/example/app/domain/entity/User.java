package com.example.app.domain.entity;

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
    @Column(name = "userPw", length = 20)
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
    @Column(name = "sns_id")
    private String snsId;
    @Column(name = "sns_connect_date")
    private String snsConnectDate;
}
