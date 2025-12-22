package com.ssafy.curtaincall.user.service;

import java.util.List;
import java.util.Optional;

import com.ssafy.curtaincall.user.dto.LikeCountDto;
import com.ssafy.curtaincall.user.dto.MyPageResponseDto;
import com.ssafy.curtaincall.user.dto.User;
import com.ssafy.curtaincall.user.dto.UserMeResponse;

public interface UserService {
	
	//조회
	List<User> getlist(); // 전체 유저 리스트 반환
	Optional<User> getUser(int id); // 유저 상세조회
//	User getEmail(String email); // 이메일 조회(중복확인용)
//	User getPhone(String phone); // 전화번호 조회(중복확인용)
	LikeCountDto getCount(int id);
	Optional<User> getUserById(int userId);
	MyPageResponseDto getMyPage(int userId);
	UserMeResponse getMyInfo(int userId);

	//회원 CUD
	int signup(User user); // 회원등록 (가입)
//	int modifyPassword(String password); // 비밀번호 변경 : 추후 암호화
	int modifyUser(User user); // 회원정보 수정
	int deleteUser(int id); // 탈퇴
	

}
