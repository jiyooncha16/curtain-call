package com.ssafy.curtaincall;

import java.util.Base64;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    private final String SECRET_KEY = // 시크릿 키 이름은 길어야 함
        Base64.getEncoder().encodeToString(
            "ssafy-curtaincall-secret-key".getBytes()
        );

    private final long EXPIRATION = 1000 * 60 * 60 * 3; // 1시간

    
    // 토큰 생성하기
    public String createToken(Authentication authentication) {

        CustomUserDetails user =
            (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
            .setSubject(user.getUsername()) // 아이디
            .claim("userId", user.getUser().getUserId()) // pk 회원번호
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),
                      SignatureAlgorithm.HS256)
            .compact();
    }
    
    // 토큰 검증하기
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    //claims 추출하기
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
