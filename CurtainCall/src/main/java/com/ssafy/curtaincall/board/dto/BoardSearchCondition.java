package com.ssafy.curtaincall.board.dto;

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
public class BoardSearchCondition {

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
	private String category; // 카테고리
	private String keyword; // 부분 검색
	
	
    
    // 정렬 조건 :  create_date / like_count / title
    private String orderBy;
    
    // 정렬 조건 : asc/desc/null
    private String order;
    
    //페이징
    private int page = 0; // 0페이지부터
    private int size = 10000; // 페이지 크기 (몇개)
    private int offset; // mybatis에서 곱셉 지원이 안 돼서 ... 여기에서 결정

    // 롬복 인식 안 돼서 수기 게터세터
    public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}

}