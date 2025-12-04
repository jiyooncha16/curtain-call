package com.ssafy.curtaincall.review.dto;

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
public class Review {

	/*
	 * ** 클래스 설명, 필드 설명 주석 필수 ** 
	 * toString : 위 어노테이션으로 대신함(@ToString)
	 * 생성자 : 위 어노테이션으로 대신함(@AllArgsConstructor)
	 * 게터, 세터 : 위 어노테이션으로 대신함(@Getter, @Setter)
	 */

	// Review : 리뷰 객체
	
	private int reviewId; // pk (아이디)
	private String content; // 내용
	private int rate; // 점수 (1~5) -- 1~5 사이인지 검사 필요
	private LocalDate createDate; // 등록일
	private int musicalId; // musicalId FK
	private int userId; // userId FK
	
	
	
}

