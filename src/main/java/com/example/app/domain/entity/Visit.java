package com.example.app.domain.entity;

import com.example.app.domain.dto.VisitDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_visit")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "visitDate", nullable = false)
    private LocalDate visitDate; // 방문일
    @Column(name = "visitNum", nullable = false)
    private int visitNum; // 방문자수

    // DTO TO ENTITY
    public static Visit toSaveEntity(VisitDto dto) {
        return Visit.builder()
                .visitDate(dto.getVisitDate())
                .visitNum(dto.getVisitNum())
                .build();
    }
}
