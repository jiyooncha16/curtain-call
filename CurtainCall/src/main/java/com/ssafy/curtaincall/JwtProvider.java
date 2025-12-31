package com.ssafy.curtaincall;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

// 배포를 위해 아래의 방식으로 변경함
//    private final String SECRET_KEY = // 시크릿 키 이름은 길어야 함
//        Base64.getEncoder().encodeToString(
//            "ssafy-curtaincall-secret-key".getBytes()
//        );
//
//    private final long EXPIRATION = 1000 * 60 * 60 * 3; // 3시간
	
	    private final Key key;
	    private final long expiration;

	    public JwtProvider(
	        @Value("${jwt.secret}") String secret,
	        @Value("${jwt.expiration}") long expiration
	    ) {
	        this.key = Keys.hmacShaKeyFor(secret.getBytes());
	        this.expiration = expiration;
	    }
    
    // 토큰 생성하기
    public String createToken(Authentication authentication) {

        CustomUserDetails user =
            (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
            .setSubject(user.getUsername()) // 아이디
            .claim("userId", user.getUser().getUserId()) // pk 회원번호
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
//            .signWith(Keys.hmacShaKeyFor(key.getBytes()),
//                      SignatureAlgorithm.HS256)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
    
    // 토큰 검증하기
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
//                .setSigningKey(key.getBytes())
            		.setSigningKey(key)
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
//            .setSigningKey(key.getBytes())
        		.setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
