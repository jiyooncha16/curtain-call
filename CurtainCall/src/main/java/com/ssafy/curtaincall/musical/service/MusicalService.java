package com.ssafy.curtaincall.musical.service;

import java.util.List;

import com.ssafy.curtaincall.musical.dto.Musical;
import com.ssafy.curtaincall.musical.dto.MusicalLikes;
import com.ssafy.curtaincall.musical.dto.SearchCondition;
import com.ssafy.curtaincall.musical.dto.Tag;

public interface MusicalService {
	
	//조회
	List<Musical> getlist(); // 전체 뮤지컬 리스트 반환
	Musical getMusical(int id); // 뮤지컬 상세조회
	List<Musical> getMusicalByCondition(SearchCondition condition); // 조건부 리스트 반환
	List<Tag> getTag(int id);

	//좋아요
	boolean toggleLike(int userId, int musicalId); // 좋아요 등록
	int getLike(int musicalId); // 좋아요 조회
	boolean isLiked(int userId, int musicalId); // 내 좋아요 확인
	List<Musical> getMyLikedMusicals(int userId);
	

}
