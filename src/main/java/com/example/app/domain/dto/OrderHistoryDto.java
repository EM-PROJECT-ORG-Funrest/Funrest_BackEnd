package com.example.app.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderHistoryDto {
    private List<OrderDto> orderDtoList;
    private List<String> proNameList;
    private List<String> imgPathList;
}
