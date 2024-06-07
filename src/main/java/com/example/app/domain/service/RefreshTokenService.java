package com.example.app.domain.service;

import com.example.app.domain.entity.RefreshToken;
import com.example.app.domain.repository.redis.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//@Service
//@RequiredArgsConstructor
//public class RefreshTokenService {
//
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    @Transactional
//    public void saveTokenInfo(String userId, String refreshToken, String accessToken){
//        refreshTokenRepository.save(new RefreshToken(userId, refreshToken, accessToken));
//    }
//
//    @Transactional
//    public void removeRefreshToken(String accessToken) {
//        refreshTokenRepository.findByAccessToken(accessToken)
//                .ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
//    }
//}

//Controller에서 사용하기 위함 -> 아직 용도가 없어서 주석 처리