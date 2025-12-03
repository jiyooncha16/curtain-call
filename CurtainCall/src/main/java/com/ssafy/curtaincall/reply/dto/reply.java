package com.ssafy.curtaincall.reply.dto;

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
public class reply {

	/*
	 * ** 클래스 설명, 필드 설명 주석 필수 ** 
	 * toString : 위 어노테이션으로 대신함(@ToString)
	 * 생성자 : 위 어노테이션으로 대신함(@AllArgsConstructor)
	 * 게터, 세터 : 위 어노테이션으로 대신함(@Getter, @Setter)
	 */

	// Reply : 뮤지컬 정보 객체
	
	private int replyId; // pk (댓글 아이디)
	private String content; // 댓글 내용
	private LocalDate create_date; // 댓글 작성일
	private int board_id; // board_id FK
	private int userId; // userId FK
	

	
	
	
	
}

