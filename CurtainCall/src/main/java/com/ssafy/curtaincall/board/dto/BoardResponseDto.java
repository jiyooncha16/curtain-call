package com.ssafy.curtaincall.board.dto;

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
public class BoardResponseDto {

	/*
	 * ** 클래스 설명, 필드 설명 주석 필수 ** 
	 * toString : 위 어노테이션으로 대신함(@ToString)
	 * 생성자 : 위 어노테이션으로 대신함(@AllArgsConstructor)
	 * 게터, 세터 : 위 어노테이션으로 대신함(@Getter, @Setter)
	 */

	// BoardResponseDto : 게시글 정보 + 닉네임까지
	
	private int boardId; // pk (게시글 아이디)
	private String title; // 게시글 제목
	private String content; // 게시글 내용
	private String category; // 게시글 카테고리
	private LocalDate createDate; // 작성일
	private int userId; // userId FK
	private String nickname;
	

	
	
	
	
}

