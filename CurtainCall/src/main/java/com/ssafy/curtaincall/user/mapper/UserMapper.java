package com.ssafy.curtaincall.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.curtaincall.actor.dto.Actor;
import com.ssafy.curtaincall.actor.dto.ActorLikes;
import com.ssafy.curtaincall.actor.dto.ActorSearchCondition;
import com.ssafy.curtaincall.user.dto.LikeCountDto;
import com.ssafy.curtaincall.user.dto.User;

@Mapper
public interface UserMapper {

	//조회
	List<User> selectAllUser();
	User selectUser(String username);
	User selectUserById(int userId);
//	User getEmail(String email);
//	User getPhone(String phone);
	LikeCountDto selectCount(int id);

	//cud
	int signup(User user);
	int updateUser(User user);
	int deleteUser(String username);
	
}
