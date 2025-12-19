package com.ssafy.curtaincall.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.user.dto.LikeCountDto;
import com.ssafy.curtaincall.user.dto.User;
import com.ssafy.curtaincall.user.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	/*
	 * @Service 달았는지 확인
	 * 주석 상세히 달기
	 * Autowired 확인
	 * 
	 */
	
	@Autowired
	UserMapper mapper;

	@Override
	public List<User> getlist() {
		return mapper.selectAllUser();
	}

	@Override
	public User getUser(String username) {
		return mapper.selectUser(username);
	}
//
//	@Override
//	public User getEmail(String email) {
//		return mapper.getEmail(email);
//	}
//
//	@Override
//	public User getPhone(String phone) {
//		return mapper.getPhone(phone);
//	}

	@Override
	public int signup(User user) {
		return mapper.signup(user);
	}

	@Override
	public int modifyUser(User user) {
		if (mapper.selectUser(user.getUsername()) == null) return 0;
		return mapper.updateUser(user);
	}

	@Override
	public int deleteUser(String username) {
		if (mapper.selectUser(username) == null) return 0;
		return mapper.deleteUser(username);
	}

	@Override
	public LikeCountDto getCount(int id) {
		
		return mapper.selectCount(id);
	}

	@Override
	public User getUserById(int userId) {
		return mapper.selectUserById(userId);
	}

}





