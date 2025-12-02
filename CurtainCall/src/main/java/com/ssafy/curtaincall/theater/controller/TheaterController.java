package com.ssafy.curtaincall.theater.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.curtaincall.musical.dto.Musical;
import com.ssafy.curtaincall.musical.dto.SearchCondition;
import com.ssafy.curtaincall.musical.service.MusicalService;
import com.ssafy.curtaincall.theater.dto.Theater;
import com.ssafy.curtaincall.theater.service.TheaterService;

@RestController
@RequestMapping("/theater")
@CrossOrigin("*")
public class TheaterController {

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
	@Qualifier("theaterServiceImpl") // 서비스 빈이 두개 찾아진다는 오류 때문에 추가했어용
	TheaterService service;
	
	// 조회
	/* 1. 전체 목록 조회 
	 * 
	 *  메서드 : GET
	 *  엔드포인트 : /musicals
	 *  파라미터
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : 없음
	 *  리턴 : List<Musical> 뮤지컬 전체 목록
	 */
	@GetMapping("")
	public ResponseEntity<List<Theater>> getlist() {
		List<Theater> list = service.getlist();
		if (list == null || list.size() == 0) return ResponseEntity.noContent().build();
		return ResponseEntity.ok(list);
	}
	
	/* 1-2. 상세 조회 - 테스트 완료
	 * 
	 *  메서드 : GET
	 *  엔드포인트 : /theater/{theater}
	 *  파라미터
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : 없음
	 *  리턴 : List<Musical> 뮤지컬 전체 목록
	 */
	@GetMapping("/{name}")
	public ResponseEntity<Theater> getMusical(@PathVariable String name) {
		Theater theater = service.getTheater(name);
		if (theater == null) return ResponseEntity.noContent().build();
		else return ResponseEntity.ok(theater);
	}
}
