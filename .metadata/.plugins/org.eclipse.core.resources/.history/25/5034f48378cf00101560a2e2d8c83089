package com.ssafy.curtaincall.musical.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.musical.dto.Musical;
import com.ssafy.curtaincall.musical.dto.MusicalLikes;
import com.ssafy.curtaincall.musical.dto.SearchCondition;
import com.ssafy.curtaincall.musical.mapper.MusicalMapper;

@Service
public class MusicalServiceImpl implements MusicalService {

	/*
	 * @Service 달았는지 확인
	 * 주석 상세히 달기
	 * Autowired 확인
	 * 
	 */
	
	@Autowired
	MusicalMapper mapper;
	
	//// 조회
	
	// 전체 목록 조회
	@Override
	public List<Musical> getlist() {
		return mapper.selectAllMusical();
	}
	
	// 개별 항목 조회
	@Override
	public Musical getMusical(int id) {
		return mapper.selectMusical(id);
	}
	
	// 조건별 조회
	@Override
	public List<Musical> getMusicalByCondition(SearchCondition condition) {
		return mapper.selectMusicalByCondition(condition);
	}
	
	
	//// 좋아요
	
	// 좋아요 등록 : 좋아요 확인 후 실행
	@Override
	public void likeOn(MusicalLikes like) {
		System.out.println("좋아요 요청 - 이미 있는 좋아요인가? : " + mapper.checkLike(like));
		if (mapper.checkLike(like) == 0) mapper.insertLike(like);		
		else System.out.println("이미 있는 좋아요입니다.");
	}
	
	//좋아요 해제 : 좋아요 확인 후 실행
	@Override
	public void likeOff(MusicalLikes like) {
		System.out.println("좋아요 삭제 요청 - 이미 있는 좋아요인가? : " + mapper.checkLike(like));
		if (mapper.checkLike(like) >= 1) mapper.deleteLike(like);	
		else System.out.println("삭제할 좋아요가 없습니다.");
	}
	

}





