package com.ssafy.curtaincall.theater.dto;

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
public class Theater {

	/*
	 * ** 클래스 설명, 필드 설명 주석 필수 ** 
	 * toString : 위 어노테이션으로 대신함(@ToString)
	 * 생성자 : 위 어노테이션으로 대신함(@AllArgsConstructor)
	 * 게터, 세터 : 위 어노테이션으로 대신함(@Getter, @Setter)
	 */

	// Theater : 공연장 정보 객체
	private String theater; // pk (공연장 이름)
	private String address; // 주소 (전체 주소, 후에 API 연동 예정)
	private boolean parking; // 주차 가능 여부
	private int seats; // 좌석 수
	private String subway; // 주변 지하철

}


