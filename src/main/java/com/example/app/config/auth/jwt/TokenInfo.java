package com.example.app.config.auth.jwt;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
    private String accessToken;
    private String refreshToken;
}
