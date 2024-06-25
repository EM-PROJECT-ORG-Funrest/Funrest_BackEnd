package com.example.app.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundDto {
    private String imp_uid;
    private String reason;
}
