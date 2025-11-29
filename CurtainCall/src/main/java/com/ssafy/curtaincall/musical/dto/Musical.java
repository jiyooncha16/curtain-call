package com.ssafy.curtaincall.musical.dto;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Musical {

	/*
	 * ** 클래스 설명, 필드 설명 주석 필수 ** 
	 * toString : 위 어노테이션으로 대신함(@ToString)
	 * 생성자 : 위 어노테이션으로 대신함(@AllArgsConstructor)
	 * 게터, 세터 : 위 어노테이션으로 대신함(@Getter, @Setter)
	 */

	// Musical : 뮤지컬 정보 객체
	
	private String musicalId; // pk (뮤지컬 아이디)
	private String title; // 뮤지컬 제목
	private LocalDate startDate; // 뮤지컬 시작일
	private LocalDate endDate; // 뮤지컬 종료일
	private String description; // 뮤지컬 설명
	private String theater; // 뮤지컬 상영 극장

}

