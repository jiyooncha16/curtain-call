package com.ssafy.curtaincall.user.mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.curtaincall.actor.dto.Actor;
import com.ssafy.curtaincall.actor.dto.ActorLikes;
import com.ssafy.curtaincall.actor.dto.ActorMeetDto;
import com.ssafy.curtaincall.actor.dto.ActorSearchCondition;
import com.ssafy.curtaincall.musical.dto.Musical;
import com.ssafy.curtaincall.user.dto.FindIdRequest;
import com.ssafy.curtaincall.user.dto.LikeCountDto;
import com.ssafy.curtaincall.user.dto.User;
import com.ssafy.curtaincall.user.dto.UserMeResponse;

@Mapper
public interface UserMapper {

	//조회
	List<User> selectAllUser();
	Optional<User> selectUser(@Param("username")String username);
	Optional<User> selectUserById(int userId);
//	User getEmail(String email);
//	User getPhone(String phone);
	LikeCountDto selectCount(int id);
	String selectNickname(int userId);
    List<Musical> selectRecentMusicals(int userId);
    List<ActorMeetDto> selectFavoriteActors(int userId);

	//cud
	int signup(User user);
	int updateUser(User user);
	int deleteUser(int id);
	
	//임시
	Map<String, Object> selectDbName();
	String selectUserByNameAndEmail(FindIdRequest req);
	
}
