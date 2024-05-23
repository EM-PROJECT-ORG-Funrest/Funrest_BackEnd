package com.example.app.domain.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotifyDto {
    private int notifyCode;
    private int proCode;
    private String userId;
    private Date notifyDate;
}
