package com.ssafy.curtaincall;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	
	//jwt 인증 필터
	
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException, java.io.IOException {

        String header = request.getHeader("Authorization");

        // 1️. 헤더 확인
        if (header != null && header.startsWith("Bearer ")) { // authorization 헤더가 Bearer로 시작하는지

            String token = header.substring(7); // Bearer 이후를 추출해서 토큰으로

            // 2️. 토큰 검증
            if (jwtProvider.validateToken(token)) {

                Claims claims = jwtProvider.getClaims(token);
                String email = claims.getSubject();

                // 3️. 유저 조회
                UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

                // 4️. 인증 객체 생성
                Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                // 5️. SecurityContext 저장
                SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            }
        }

        // 필터 체인에 연결
        filterChain.doFilter(request, response);
    }
}

