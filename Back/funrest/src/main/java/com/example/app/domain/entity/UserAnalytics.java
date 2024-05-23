package com.example.app.domain.entity;

import com.example.app.domain.dto.UserAnalyticsDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_userAnalytics")
public class UserAnalytics {
    @Id
    @Column(name = "visitDate", nullable = false)
    private Date visitDate; // 방문일
    @Column(name = "visitUser")
    private int visitUser; // 방문자수

    // DTO TO ENTITY
    public static UserAnalytics toEntity(UserAnalyticsDto dto) {
        return UserAnalytics.builder()
                .visitDate(dto.getVisitDate())
                .visitUser(dto.getVisitUser())
                .build();
    }
}
