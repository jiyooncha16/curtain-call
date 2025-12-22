package com.ssafy.curtaincall.actor.service;

import java.util.List;

import com.ssafy.curtaincall.actor.dto.Actor;
import com.ssafy.curtaincall.actor.dto.ActorCastingDto;
import com.ssafy.curtaincall.actor.dto.ActorLikes;
import com.ssafy.curtaincall.actor.dto.ActorSearchCondition;
import com.ssafy.curtaincall.actor.dto.Casting;
import com.ssafy.curtaincall.actor.dto.RelatedActorDto;

public interface ActorService {
	
	//조회
	List<Actor> getlist(); // 전체 뮤지컬 리스트 반환
	Actor getActor(int id); // 뮤지컬 상세조회
	List<Actor> getActorByCondition(ActorSearchCondition condition); // 조건부 리스트 반환
	List<Actor> getActorOfTopFive(); // 상위 5개
	List<ActorCastingDto> getCasting(int id); // 캐스팅 받기
	List<RelatedActorDto> getRelatedActors(int id);

	//좋아요
	void likeOn(ActorLikes like); // 좋아요 등록
	void likeOff(ActorLikes like); // 좋아요 해제
	

}
