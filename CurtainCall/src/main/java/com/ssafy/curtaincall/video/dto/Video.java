package com.ssafy.curtaincall.video.dto;

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
public class Video {

	/*
	 * ** 클래스 설명, 필드 설명 주석 필수 ** 
	 * toString : 위 어노테이션으로 대신함(@ToString)
	 * 생성자 : 위 어노테이션으로 대신함(@AllArgsConstructor)
	 * 게터, 세터 : 위 어노테이션으로 대신함(@Getter, @Setter)
	 */

	// Video : 비디오 정보 객체
	
	private int videoId; // pk (비디오 아이디)
	private int musicalId; // fk (뮤지컬 아이디)
	private String url; // 영상 url
	private String title; // 비디오 제목
	private int viewCnt; // 조회수
	

	
	
	
	
}

