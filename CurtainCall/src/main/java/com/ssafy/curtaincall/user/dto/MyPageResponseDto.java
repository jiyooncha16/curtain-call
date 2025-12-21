package com.ssafy.curtaincall.user.dto;

import java.util.List;

import com.ssafy.curtaincall.ai.dto.HashtagDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPageResponseDto {

    private UserResponse user;          // 기본 유저 정보
    private List<HashtagDTO> tags;     // 해시태그
    private String taste;          // 취향 설명
    private LikeCountDto counts;       // 통계
}