package com.ssafy.curtaincall.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nimbusds.oauth2.sdk.TokenResponse;
import com.ssafy.curtaincall.CustomUserDetails;
import com.ssafy.curtaincall.JwtProvider;
import com.ssafy.curtaincall.ai.dto.HashtagDTO;
import com.ssafy.curtaincall.ai.service.AiService;
import com.ssafy.curtaincall.user.dto.LikeCountDto;
import com.ssafy.curtaincall.user.dto.LoginRequest;
import com.ssafy.curtaincall.user.dto.LoginResponse;
import com.ssafy.curtaincall.user.dto.MyPageResponseDto;
import com.ssafy.curtaincall.user.dto.User;
import com.ssafy.curtaincall.user.dto.UserMeResponse;
import com.ssafy.curtaincall.user.dto.UserResponse;
import com.ssafy.curtaincall.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
	
	////////////Spring Security 배운 후 비밀번호에 Bcrypt 적용하기
	///////////로그인도 추가하기
	
	/*
	 * 서비스 의존성 주입 필수 (@Autowired)
	 * 각 항목 정리해서 주석 상세하게 달기
	 * - 번호 정리
	 * - 각 항목의 파라미터와 리턴 값
	 * 
	 * RESTful 하게 작성 (get, post, put, delete)
	 * url에 주의할 것 (단, http메서드가 다르면 url은 겹쳐도 됨)
	 * 
	 * 리턴은 결과값 고려(수정됐는지, 리스트가 비었는지 등)하여
	 * ResponseEntity로 반환
	 * 
	 * 200(ok) : ResponseEntity.ok()
	 * 200(ok) + 리턴값 : ResponseEntity.ok(list)
	 * 
	 * 401(noContent) : ResponseEntity.noContent()
	 * 401(noContent) + 메시지 : ResponseEntity.status(HttpStatus.noContent).body("내용이 없습니다. (401)")
	 * 
	 * 404(notFound) : ResponseEntity.notFound()
	 * 404(notFound) + 메시지 : ResponseEntity.status(HttpStatus.notFound).body("요청하신 내용을 찾지 못했습니다. (404)")
	 */
	
	@Autowired
	@Qualifier("userServiceImpl")
	UserService service;
	

	@Autowired
	@Qualifier("aiServiceImpl")
	AiService aiService;
	

	private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
	
	// 1. 조회
	/* 1-1. 전체 목록 조회
	 * 
	 *  메서드 : GET
	 *  엔드포인트 : /user/list
	 *  파라미터
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : 없음
	 *  리턴 : List<User> 
	 */
	@GetMapping("/list")
	public ResponseEntity<List<User>> getlist() {
		List<User> list = service.getlist();
		if (list == null || list.size() == 0) return ResponseEntity.noContent().build();
		return ResponseEntity.ok(list);
	}
	
	/* 1-2. 상세 조회
	 * 
	 *  메서드 : GET
	 *  엔드포인트 : /user/{username}
	 *  파라미터
	 *   - pathVariable(url) : username
	 *   - RequestBody(json) : 없음
	 *  리턴 : User
	 */
//	@GetMapping("/{username}")
//	public ResponseEntity<Optional<User>> getUser(@PathVariable String username) {
//		Optional<User> user = service.getUser(username);
//		if (user == null) return ResponseEntity.noContent().build();
//		else return ResponseEntity.ok(user);
//	}
	
//	/* 1-2-2. 상세 조회
//	 * 
//	 *  메서드 : GET
//	 *  엔드포인트 : /user/id/{userId}
//	 *  파라미터
//	 *   - pathVariable(url) : userId
//	 *   - RequestBody(json) : 없음
//	 *  리턴 : User
//	 */
//	@GetMapping("/id/{userId}")
//	public ResponseEntity<User> getUserById(@PathVariable int userId) {
//		User user = service.getUserById(userId);
//		if (user == null) return ResponseEntity.noContent().build();
//		else return ResponseEntity.ok(user);
//	}

	/* 1-3. count 조회
	 * 
	 *  메서드 : GET
	 *  엔드포인트 : /user/count/{id}
	 *  파라미터
	 *   - pathVariable(url) : id
	 *   - RequestBody(json) : 없음
	 *  리턴 : User
	 */
	@GetMapping("/count/{id}")
	public ResponseEntity<LikeCountDto> getCount(@PathVariable int id) {
		LikeCountDto dto = service.getCount(id);
		if (dto == null) return ResponseEntity.noContent().build();
		else return ResponseEntity.ok(dto);
	}
	
	/* 1-4. 해시태그 조회
	 * 
	 *  메서드 : GET
	 *  엔드포인트 : /user/tag/{id}
	 *  파라미터
	 *   - pathVariable(url) : id
	 *   - RequestBody(json) : 없음
	 *  리턴 : User
	 */
	@GetMapping("/tag/{id}")
	public ResponseEntity<List<HashtagDTO>> getTag(@PathVariable int id) {
		List<HashtagDTO> dto = aiService.hashtagCount(id);
		if (dto == null) return ResponseEntity.noContent().build();
		else return ResponseEntity.ok(dto);
	}
	
	
	// 2. 회원가입(등록)
	/* 
	 *  메서드 : POST
	 *  엔드포인트 : /user
	 *  파라미터
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : User
	 *  리턴 : 없음
	 *  
	 *  ///////// 유효성 검사 : 아이디 8자리 이상
	 */
	@PostMapping("")
	public ResponseEntity<?> signup(@Valid @RequestBody User user) {// 유효성 검사
		int result = service.signup(user);

	    System.out.println("insert result = " + result);
		if (result == 1) return ResponseEntity.ok(user);
		return ResponseEntity.badRequest().body("잘못된 입력입니다.");
	}
	
	
	// 3. 회원수정 // 아이디는 수정 불가능
	/* 
	 *  메서드 : PUT
	 *  엔드포인트 : /user
	 *  파라미터
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : User
	 *  리턴 : 없음
	 */
	@PutMapping("/me")
	public ResponseEntity<?> modifyMe(
	    @RequestBody User user,
	    @AuthenticationPrincipal CustomUserDetails loginUser
	) {
		if (user == null) {
	        return ResponseEntity.status(401).build();
	    }
		user.setUserId(loginUser.getUserId());
	    service.modifyUser(user);
	    return ResponseEntity.ok().build();
	}
	
	
	//4. 회원 삭제
	/* 
	 *  메서드 : DELETE
	 *  엔드포인트 : /user/{username}
	 *  파라미터
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : username(실제 아이디)
	 *  리턴 : 없음
	 */
	@DeleteMapping("/me")
	public ResponseEntity<?> deleteMe(
	    @AuthenticationPrincipal CustomUserDetails user
	) {
	    service.deleteUser(user.getUserId());
	    return ResponseEntity.ok().build();
	}
	//5. AI : 유저 수식어 만들기
	@GetMapping("/taste/{id}")
	public String getUserTaste(@PathVariable int id) {
		System.out.println("컨트롤러 : 시작됐어요");
		String result = aiService.generateUserTaste(id);
		if (result == null) System.out.println("오류가 발생했습니다.");
		else System.out.println(result);
		return result;
	}
	
	// 6. 로그인 (spring security)
	@PostMapping("/auth/login")
	public LoginResponse login(@RequestBody LoginRequest req) {

        Authentication authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    req.getUsername(),
                    req.getPassword()
                )
            );
        String token = jwtProvider.createToken(authentication);
        return new LoginResponse(token);
    }
	
	// 7. 현재 로그인한 유저 정보 반환
	@GetMapping("/me")
	public MyPageResponseDto me(@AuthenticationPrincipal CustomUserDetails user) {
	        return service.getMyPage(user.getUserId());
	}

	// 8. 현재 로그인한 유저 정보 반환 (메인 페이지 전용)
	@GetMapping("/main")
	public ResponseEntity<?> main(@AuthenticationPrincipal CustomUserDetails userDetails) {

	    int userId = userDetails.getUserId();
	    UserMeResponse response = service.getMyInfo(userId);
	    return ResponseEntity.ok(response);
	}

}
