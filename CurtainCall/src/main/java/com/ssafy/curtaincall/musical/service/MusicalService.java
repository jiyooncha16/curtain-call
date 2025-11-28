package com.ssafy.curtaincall.musical.service;

import java.util.List;

import com.ssafy.curtaincall.musical.dto.Musical;
import com.ssafy.curtaincall.musical.dto.MusicalLikes;
import com.ssafy.curtaincall.musical.dto.SearchCondition;

public interface MusicalService {

	/*
	 * 서비스 인터페이스
	 * - 주석 작성할 것
	 * 
	 */
	
	//조회
	List<Musical> getlist(); // 전체 뮤지컬 리스트 반환
	Musical getMusical(int id); // 뮤지컬 상세조회
	List<Musical> getMusicalByCondition(SearchCondition condition); // 조건부 리스트 반환

	//좋아요
	void likeOn(MusicalLikes like); // 좋아요 등록
	void likeOff(MusicalLikes like); // 좋아요 해제
	

}
