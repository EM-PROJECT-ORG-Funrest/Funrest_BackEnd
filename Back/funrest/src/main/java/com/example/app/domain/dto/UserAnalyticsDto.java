package com.example.app.domain.dto;

import com.example.app.domain.entity.UserAnalytics;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnalyticsDto {
    private Date visitDate; // 방문일
    private int visitUser; // 방문자수

    // ENTITY TO DTO
    public static UserAnalyticsDto toDto(UserAnalytics entity) {
        return UserAnalyticsDto.builder()
                .visitDate(entity.getVisitDate())
                .visitUser(entity.getVisitUser())
                .build();
    }
}
