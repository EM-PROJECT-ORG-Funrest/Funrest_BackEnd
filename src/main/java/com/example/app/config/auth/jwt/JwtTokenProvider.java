package com.example.app.config.auth.jwt;

import com.example.app.config.auth.PrincipalDetailsService;
import com.example.app.domain.entity.RefreshToken;
import com.example.app.domain.repository.redis.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource("classpath:application-SECRET-KEY.properties")
public class JwtTokenProvider implements InitializingBean {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PrincipalDetailsService principalDetailsService;

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMilliseconds; // ACCESS TOKEN 만료 시간 +) 600(test용) -> 3600으로 바꾸기
    private final long refreshTokenValidityInMilliseconds; // REFRESH TOKEN 만료 시간

    private Key key;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInMilliseconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000; // 60 * 60 * 24 * 1000
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000; // 60 * 60 * 24 * 3 * 1000
    }

    // 빈 생성이 되고 의존성 주입 받은 secret 값을 Base64 Decode 해서 key 변수에 할당
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Authentication의 권한 정보 이용해서 토큰(access Token) 생성
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("userId", authentication.getName())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // refresh token - 액세스 토큰을 재발급해주는 용도이므로, 사용자 정보 담을 필요 X
    public String createRefreshToken() {
        Claims claims = Jwts.claims();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidityInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    // 토큰에 담겨 있는 정보를 이용해 Authentication 객체 리턴
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰의 유효성 검증 및 토큰을 파싱하여 exception을 캐치
    public boolean validateToken(String accessToken, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명");
        } catch(ExpiredJwtException e) {
            log.info("만료된 JWT 토큰 : " + accessToken);

            // redis에 저장되어 있는 토큰 정보를 만료된 access token으로 찾아옴
            RefreshToken foundTokenInfo = refreshTokenRepository.findByAccessToken(accessToken)
                    .orElseThrow(() -> new IllegalArgumentException("Token not found"));

            String refreshToken = foundTokenInfo.getRefreshToken();
            log.info(refreshToken);

            if(refreshToken.isEmpty() || refreshToken == null) {
                // refresh token이 만료 됐을 경우
                log.info("refresh token not found");
                logout(request, response);
                return false;
            }

            log.info("access token 재발급 처리중");
            // refresh token이 유효하다면, redis에 함께 저장해둔 userId를 가져옴
            String userId = foundTokenInfo.getId();

            UserDetails userDetails = principalDetailsService.loadUserByUsername(userId);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 다시 access token 만들어 발급
            accessToken = createToken(authentication);

            // 새로 발급받은 access token으로 Redis도 업데이트
            refreshTokenRepository.save(new RefreshToken(userId, refreshToken, accessToken));
            // 클라이언트 측 쿠키의 access token도 업데이트
            Cookie cookie = new Cookie(JwtAuthorizationFilter.AUTHORIZATION_HEADER, accessToken);
            cookie.setMaxAge(86400);
            cookie.setPath("/");
            response.addCookie(cookie);
            // 근데 액세스 토큰 만료 -> 액세스 토큰 재발급, 리프레스 토큰 재발급, 쿠키 재생성
            // -> 로그아웃 버튼 누르지 않는 이상 로그인 상태가 계속 유지 -> 이게 맞나..?
            response.setContentType("text/html");
            response.getWriter().write("<script>location.reload();</script>");
            response.getWriter().flush();
            response.getWriter().close();
        } catch(UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰");
        } catch(IllegalArgumentException e) {
            log.info("잘못된 JWT 토큰");
        }
        return false;
    }

    // access token 만료 & refresh token 만료일 경우 로그아웃 처리
    private void logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        Cookie cookie = new Cookie(JwtAuthorizationFilter.AUTHORIZATION_HEADER, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
