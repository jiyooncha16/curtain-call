package com.ssafy.curtaincall.review.dto;

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
public class ReviewSearchCondition {

	/*
	 * ** 클래스 설명, 필드 설명 주석 필수 ** 
	 * toString : 위 어노테이션으로 대신함(@ToString)
	 * 생성자 : 위 어노테이션으로 대신함(@AllArgsConstructor)
	 * 게터, 세터 : 위 어노테이션으로 대신함(@Getter, @Setter)
	 */

	// SearchCondition : 뮤지컬 검색 조건
	
	/*
	 * 	세부내용
	 *  - 정렬 기준 : 스네이크 케이스(create_date) 식으로 적어야 함
	 *  - 페이징 : 몇페이지, 몇개씩
	 */
	
	
	//어떤 뮤지컬
	private int musicalId;

	// 정렬
	private String orderBy = "create_date";
	private String order = "DESC";
    
    //페이징
    private int page = 0; // 0페이지부터
    private int size = 10; // 페이지 크기 (몇개)
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