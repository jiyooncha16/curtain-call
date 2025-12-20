package com.ssafy.curtaincall.actor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.actor.dto.Actor;
import com.ssafy.curtaincall.actor.dto.ActorLikes;
import com.ssafy.curtaincall.actor.dto.ActorSearchCondition;
import com.ssafy.curtaincall.actor.dto.Casting;
import com.ssafy.curtaincall.actor.mapper.ActorMapper;

@Service
public class ActorServiceImpl implements ActorService {

	/*
	 * @Service 달았는지 확인
	 * 주석 상세히 달기
	 * Autowired 확인
	 * 
	 */
	
	@Autowired
	ActorMapper mapper;
	
	//// 조회
	
	// 전체 목록 조회
	@Override
	public List<Actor> getlist() {
		return mapper.selectAllActor();
	}
	
	// 개별 항목 조회
	@Override
	public Actor getActor(int id) {
		return mapper.selectActor(id);
	}
	
	// 조건별 조회
	@Override
	public List<Actor> getActorByCondition(ActorSearchCondition condition) {
		System.out.println(condition.toString());
		return mapper.selectActorByCondition(condition);
	}
	
	
	//// 좋아요
	
	// 좋아요 등록 : 좋아요 확인 후 실행
	@Override
	public void likeOn(ActorLikes like) {
		System.out.println("좋아요 요청 - 이미 있는 좋아요인가? : " + mapper.checkLike(like));
		if (mapper.checkLike(like) == 0) mapper.insertLike(like);		
		else System.out.println("이미 있는 좋아요입니다.");
	}
	
	//좋아요 해제 : 좋아요 확인 후 실행
	@Override
	public void likeOff(ActorLikes like) {
		System.out.println("좋아요 삭제 요청 - 이미 있는 좋아요인가? : " + mapper.checkLike(like));
		if (mapper.checkLike(like) >= 1) mapper.deleteLike(like);	
		else System.out.println("삭제할 좋아요가 없습니다.");
	}

	@Override
	public List<Actor> getActorOfTopFive() {
		return mapper.selectActorOfTopFive();
	}

	@Override
	public List<Casting> getCasting(int id) {
		return mapper.selectMusicalActor(id);
	}
	

}





