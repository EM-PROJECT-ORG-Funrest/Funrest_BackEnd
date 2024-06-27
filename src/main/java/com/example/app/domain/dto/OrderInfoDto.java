package com.example.app.domain.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInfoDto {
    LocalDate orderDate;
    Long orderCnt;
    Long totalAmount;
}
