package com.ssafy.curtaincall.actor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.curtaincall.actor.dto.Actor;
import com.ssafy.curtaincall.actor.dto.ActorCastingDto;
import com.ssafy.curtaincall.actor.dto.ActorLikes;
import com.ssafy.curtaincall.actor.dto.ActorSearchCondition;
import com.ssafy.curtaincall.actor.dto.Casting;
import com.ssafy.curtaincall.actor.dto.RelatedActorDto;
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
	
	
	@Override
	public List<Actor> getActorOfTopFive() {
		return mapper.selectActorOfTopFive();
	}

	@Override
	public List<ActorCastingDto> getCasting(int id) {
		return mapper.selectMusicalActor(id);
	}

	@Override
	public List<RelatedActorDto> getRelatedActors(int id) {
	    return mapper.getRelatedActors(id);
	}


	//// 좋아요
	@Override
	@Transactional
	public boolean toggleLike(int userId, int actorId) {

	    int count = mapper.existsLike(userId, actorId);

        if (count > 0) {
        	mapper.deleteLike(userId, actorId); // 눌렀으면 취소
            return false;
            
        } else {
        	mapper.insertLike(userId, actorId); // 안 눌렀으면 추가
            return true;
        }
	}

	@Override
	public boolean isLiked(int userId, int actorId) {
		return mapper.existsLike(userId, actorId) > 0;
	}

	@Override
	public int getLike(int actorId) {
		return mapper.selectLike(actorId);
	}

	@Override
	public List<Actor> getMyLikedActors(int userId) {
		// TODO Auto-generated method stub
		return mapper.selectLikedActor(userId);
	}

	@Override
	public List<Actor> getOnStageActors() {
		return mapper.selectOnStageActor();
	}

}





