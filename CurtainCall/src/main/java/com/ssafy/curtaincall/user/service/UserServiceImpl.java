package com.ssafy.curtaincall.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.curtaincall.ai.dto.HashtagDTO;
import com.ssafy.curtaincall.ai.service.AiService;
import com.ssafy.curtaincall.user.dto.LikeCountDto;
import com.ssafy.curtaincall.user.dto.MyPageResponseDto;
import com.ssafy.curtaincall.user.dto.User;
import com.ssafy.curtaincall.user.dto.UserMeResponse;
import com.ssafy.curtaincall.user.dto.UserResponse;
import com.ssafy.curtaincall.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {


	/*
	 * @Service 달았는지 확인
	 * 주석 상세히 달기
	 * Autowired 확인
	 * 
	 */
	
//	@Autowired
//	UserMapper mapper;
//	
//	@Autowired
//	AiService aiService;
	
	private final UserMapper mapper;
    private final AiService aiService;
    private final PasswordEncoder passwordEncoder;

	@Override
	public List<User> getlist() {
		return mapper.selectAllUser();
	}

	@Override
	public Optional<User> getUser(int id) {
		return mapper.selectUserById(id);
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
		String encodedPw = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPw);
		return mapper.signup(user);
	}

	@Override
	public int modifyUser(User user) {
		if (mapper.selectUserById(user.getUserId()) == null) return 0;
		if (user.getPassword() != null && !user.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(user.getPassword());
            user.setPassword(encoded);
        } else {
        	user.setPassword(null); // SQL에서 제외
        }
		return mapper.updateUser(user);
	}

	@Override
	public int deleteUser(int id) {
		if (mapper.selectUserById(id) == null) return 0;
		return mapper.deleteUser(id);
	}

	@Override
	public LikeCountDto getCount(int id) {
		
		return mapper.selectCount(id);
	}

	@Override
	public Optional<User> getUserById(int userId) {
		return mapper.selectUserById(userId);
	}
	
	@Override
	public MyPageResponseDto getMyPage(int userId) {

        User user = mapper.selectUserById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        UserResponse userDto = new UserResponse(
    		  	user.getUserId(),
	            user.getUsername(),
	            user.getName(),
	            user.getEmail(),
	            user.getPhone(),
	            user.getNickname(),
	            user.getProfileImage()

        );
        List<HashtagDTO> tags = aiService.hashtagCount(userId);

        String taste = aiService.generateUserTaste(userId);

        LikeCountDto counts = mapper.selectCount(userId);

        return new MyPageResponseDto(userDto, tags, taste, counts);
    }
	


	@Override
    public UserMeResponse getMyInfo(int userId) {

        UserMeResponse res = new UserMeResponse();

        // 닉네임
        String nickname = mapper.selectNickname(userId);
        res.setNickname(nickname);

        // 최근 본 뮤지컬
        res.setRecentMusicals(mapper.selectRecentMusicals(userId));

        // 자주 본 배우
        res.setFavoriteActors(mapper.selectFavoriteActors(userId));

        return res;
    }

}





