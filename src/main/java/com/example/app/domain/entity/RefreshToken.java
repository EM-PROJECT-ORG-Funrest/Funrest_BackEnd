package com.example.app.domain.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 3)
public class RefreshToken {
    @Id
    private String id;
    private String refreshToken;
    @Indexed
    private String accessToken; // 만료된 액세스 토큰으로 refresh Token을 찾아와 유효성 검사
}
