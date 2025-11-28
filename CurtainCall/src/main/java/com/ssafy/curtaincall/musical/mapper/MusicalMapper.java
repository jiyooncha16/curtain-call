package com.ssafy.curtaincall.musical.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.curtaincall.musical.dto.Musical;
import com.ssafy.curtaincall.musical.dto.MusicalLikes;
import com.ssafy.curtaincall.musical.dto.SearchCondition;

@Mapper
public interface MusicalMapper {
	
	//조회
	public List<Musical> selectAllMusical(); // 전체 목록
	public List<Musical> selectMusicalByCondition(SearchCondition condition); // 조건부 목록(검색)
	public Musical selectMusical(int id); // 뮤지컬 상세조회
	
	//좋아요
	public int checkLike(MusicalLikes like); // 좋아요 찍혀있는지 확인 (개수 반환)
	public void insertLike(MusicalLikes like); // 좋아요 등록
	public void deleteLike(MusicalLikes like); // 좋아요 해제
	
}
