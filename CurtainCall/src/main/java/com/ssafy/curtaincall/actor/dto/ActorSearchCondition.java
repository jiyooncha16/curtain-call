package com.ssafy.curtaincall.actor.dto;

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
public class ActorSearchCondition {

	/*
	 * ** 클래스 설명, 필드 설명 주석 필수 ** 
	 * toString : 위 어노테이션으로 대신함(@ToString)
	 * 생성자 : 위 어노테이션으로 대신함(@AllArgsConstructor)
	 * 게터, 세터 : 위 어노테이션으로 대신함(@Getter, @Setter)
	 */

	// ActorSearchCondition : 배우 검색 조건
	
	/*
	 * 	세부내용
	 *  - 필터링 : 이름별
	 *  - 순서 : 좋아요, id(등록순)
	 *  - 페이징 : 몇페이지, 몇개씩
	 */
	
	//필터
	private String name; // 이름
    
    // 정렬 조건 : 좋아요/아이디(등록 순서)
    private String orderBy;
    
    // 정렬 조건 : asc/desc/null
    private String order;
    
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