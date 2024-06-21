package com.example.app.domain.dto;

import com.example.app.domain.entity.Visit;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDto {
    private LocalDate visitDate; // 방문일
    private int visitNum; // 방문자수

    // ENTITY TO DTO
    public static VisitDto toSaveDto(Visit entity) {
        return VisitDto.builder()
                .visitDate(entity.getVisitDate())
                .visitNum(entity.getVisitNum())
                .build();
    }
}
