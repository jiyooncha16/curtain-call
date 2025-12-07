package com.ssafy.curtaincall.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.curtaincall.user.dto.User;
import com.ssafy.curtaincall.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
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
	@GetMapping("/{username}")
	public ResponseEntity<User> getUser(@PathVariable String username) {
		User user = service.getUser(username);
		if (user == null) return ResponseEntity.noContent().build();
		else return ResponseEntity.ok(user);
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
	@PutMapping("")
	public ResponseEntity<?> modifyUser(@Valid @RequestBody User user) {// 유효성 검사
		int result = service.modifyUser(user);
		if (result == 1) return ResponseEntity.ok(user);
		return ResponseEntity.badRequest().body("잘못된 입력입니다.");
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
	@DeleteMapping("/{username}")
	public ResponseEntity<?> deleteUser(@PathVariable String username) {
		int result = service.deleteUser(username);
		if (result == 1) return ResponseEntity.ok(username);
		return ResponseEntity.badRequest().body("잘못된 입력입니다.");
	}
	

}
