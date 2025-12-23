package com.ssafy.curtaincall.musical.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.curtaincall.musical.dto.Musical;
import com.ssafy.curtaincall.musical.dto.MusicalLikes;
import com.ssafy.curtaincall.musical.dto.SearchCondition;
import com.ssafy.curtaincall.musical.dto.Tag;

@Mapper
public interface MusicalMapper {
	
	//조회
	public List<Musical> selectAllMusical(); // 전체 목록
	public List<Musical> selectMusicalByCondition(SearchCondition condition); // 조건부 목록(검색)
	public Musical selectMusical(int id); // 뮤지컬 상세조회
	public List<Tag> selectTag(int id);
	
	//좋아요
//	public void insertLike(MusicalLikes like); // 좋아요 등록
//	public void deleteLike(MusicalLikes like); // 좋아요 해제
	public int selectLike(int musicalId); // 이 뮤지컬의 좋아요 개수
	public int existsLike(@Param("userId")int userId, @Param("musicalId")int musicalId); // 내 좋아요
	public void deleteLike(@Param("userId")int userId, @Param("musicalId")int musicalId);
	public void insertLike(@Param("userId")int userId, @Param("musicalId")int musicalId);
	
}
