package com.ssafy.curtaincall.user.dto;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {

	/*
	 * ** 클래스 설명, 필드 설명 주석 필수 ** 
	 * toString : 위 어노테이션으로 대신함(@ToString)
	 * 생성자 : 위 어노테이션으로 대신함(@AllArgsConstructor)
	 * 게터, 세터 : 위 어노테이션으로 대신함(@Getter, @Setter)
	 */

	// User : 회원 정보 객체
	@Size(min = 8, message = "아이디는 8자리 이상이어야 합니다.")
	@NotBlank(message = "아이디는 필수 입력값입니다.")
	private String username; // 아이디 -- 유효성 검사 필요
	
	@NotBlank(message = "비밀번호는 필수 입력값입니다.")
	private String password; // 비밀번호 -- 추후 bcrypt 처리 필요

	@NotBlank(message = "이름은 필수 입력값입니다.")
	private String name; 

	@Email(message = "올바른 이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일은 필수 입력값입니다.")
	private String email; // 이메일 -- 얘도 유효성 검사 필요

	@Size(min = 11, max = 11, message = "휴대폰 번호는 11자리여야 합니다.")
	@NotBlank(message = "전화번호는 필수 입력값입니다.")
	private String phone; // 전화번호 -- 얘도 (자리수)
	
	@NotBlank(message = "닉네임은 필수 입력값입니다.")
	private String nickname;
	
}

