package com.ssafy.curtaincall.musical.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SearchCondition {

	/*
	 * ** 클래스 설명, 필드 설명 주석 필수 ** 
	 * toString : 위 어노테이션으로 대신함(@ToString)
	 * 생성자 : 위 어노테이션으로 대신함(@AllArgsConstructor)
	 * 게터, 세터 : 위 어노테이션으로 대신함(@Getter, @Setter)
	 */

	// SearchCondition : 뮤지컬 검색 조건
	
	/*
	 * 	세부내용
	 *  - 필터링 : 해시태그별, 키워드별, 극장별, 날짜별
	 *  - 순서 : 좋아요 랜덤
	 *  - 페이징 : 몇페이지, 몇개씩
	 */
	
	//필터
	private List<String> hashtag; // 해시태그
	private String keyword; // 키워드0
	private String theater; // 극장
	
	// 날짜 필터
	private LocalDate startDate;   // 포함 이후 공연
    private LocalDate endDate;     // 포함 이전 공연
    
    // 정렬 조건 : 좋아요/랜덤/아이디(등록 순서)
    private String orderBy;
    
    // 정렬 조건 : asc/desc/null
    private String order;
    
    //페이징
    private int page; // 0페이지부터
    private int size; // 페이지 크기 (몇개)
}