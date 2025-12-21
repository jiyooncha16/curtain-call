package com.ssafy.curtaincall.user.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
	
	// 로그인을 위한 dto
    private String username;
    private String password;
}