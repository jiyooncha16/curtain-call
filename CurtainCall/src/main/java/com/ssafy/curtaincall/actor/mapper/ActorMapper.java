package com.ssafy.curtaincall.actor.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.curtaincall.actor.dto.Actor;
import com.ssafy.curtaincall.actor.dto.ActorCastingDto;
import com.ssafy.curtaincall.actor.dto.ActorLikes;
import com.ssafy.curtaincall.actor.dto.ActorSearchCondition;
import com.ssafy.curtaincall.actor.dto.Casting;
import com.ssafy.curtaincall.actor.dto.RelatedActorDto;

@Mapper
public interface ActorMapper {
	
	//조회
	public List<Actor> selectAllActor(); // 전체 목록
	public List<Actor> selectActorByCondition(ActorSearchCondition condition); // 조건부 목록(검색)
	public Actor selectActor(int id); // 뮤지컬 상세조회
	public List<Actor> selectActorOfTopFive();
	public List<ActorCastingDto> selectMusicalActor(int id);
	  List<RelatedActorDto> getRelatedActors(
		        @Param("actorId") int actorId
		    );
	
	//좋아요

		public int selectLike(int actorId); // 이 뮤지컬의 좋아요 개수
		public int existsLike(@Param("userId")int userId, @Param("actorId")int actorId); // 내 좋아요
		public void deleteLike(@Param("userId")int userId, @Param("actorId")int actorId);
		public void insertLike(@Param("userId")int userId, @Param("actorId")int actorId);
		public List<Actor> selectLikedActor(int userId);
	
}
