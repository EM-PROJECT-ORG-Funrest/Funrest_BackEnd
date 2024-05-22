package com.example.app.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionDto {
    private String sessionId;
    private String userId;
}
