package com.ssafy.curtaincall.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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

import com.ssafy.curtaincall.board.dto.Board;
import com.ssafy.curtaincall.board.dto.BoardLikes;
import com.ssafy.curtaincall.board.dto.BoardSearchCondition;
import com.ssafy.curtaincall.board.service.BoardService;
import com.ssafy.curtaincall.review.dto.Review;
import com.ssafy.curtaincall.review.dto.ReviewLikes;
import com.ssafy.curtaincall.review.dto.ReviewSearchCondition;
import com.ssafy.curtaincall.review.service.ReviewService;

@RestController
@RequestMapping("/reviews")
@CrossOrigin("*")
public class ReviewController {
	
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
	@Qualifier("reviewServiceImpl")
	ReviewService service;
	
	/* 1-1. 뮤지컬별 리뷰 목록 조회
	 *
	 * 메서드 : GET
	 * 엔드포인트 : /reviews
	 * 파라미터
	 *   - pathVariable(url) : 뮤지컬 id
	 *   - ModelAttribute(쿼리스트링) : 필터/정렬 조건
	 *   	- 좋아요 기준 여부
	 *   	- 페이징
	 * 리턴 : List<Board> 게시글 목록기본 10개씩
	 * 
	 * 사용처
	 * - 리뷰 보여주기(날짜 역순) (http://localhost:8080/reviews/{musicalId}) 
	 * - 리뷰 인기순 보여주기(좋아요 역순) 
	 * 	- 인기순 상위 5개인 경우	
	 * 		(http://localhost:8080/reviews/{musicalId}?likes=true&size=5)
	 */
	@GetMapping("/{musicalId}")
	public ResponseEntity<List<Review>> getlist(@PathVariable int musicalId, @ModelAttribute ReviewSearchCondition condition) {
		condition.setMusicalId(musicalId);
		List<Review> list = service.getReviewByCondition(condition);
		if (list == null || list.size() == 0) return ResponseEntity.noContent().build();
		return ResponseEntity.ok(list);
	}
	
	
	/* 1-2. 리뷰 평점 조회
	 *
	 * 메서드 : GET
	 * 엔드포인트 : /reviews/rate/{id}
	 * 파라미터
	 *   - PathVariable(id) : 조회할 게시글 번호
	 *   - RequestBody(json) : 없음
	 * 리턴 : 리뷰 평점 (double, 소수점 아래 한자리까지)
	 */
	@GetMapping("/rating/{id}")
	public ResponseEntity<Double> getReviewRate(@PathVariable int id) {
		double rate = service.getReviewRate(id);
		if (rate == 0.0 || rate == 0) return ResponseEntity.noContent().build();
		else return ResponseEntity.ok((Double)rate);
	}
	
	
	
	
	

	// 2. CUD 
	/* 2-1. 리뷰 등록 
	 * 메서드 : POST
	 * 엔드포인트 : /reviews/{musicalId}
	 * 파라미터
	 *   - RequestBody(json) : review
	 * 리턴 : 게시글 생성 결과 메시지(ResponseEntity)
	 */
	@PostMapping("/{musicalId}")
	public ResponseEntity<?> createReview(@PathVariable int musicalId, @RequestBody Review review) {
	    review.setMusicalId(musicalId);
		int id = service.createReview(review);
	    if (id != -1) {
	        return ResponseEntity.ok(id);
	    }
	    return ResponseEntity.badRequest().body("리뷰 생성 실패 : 이미 해당 뮤지컬에 대한 리뷰를 작성하셨니다.");
	}

	

	 /*
	 * 2-2. 리뷰 수정
	 * 메서드 : PUT
	 * 엔드포인트 : /reviews/{id}
	 * 파라미터
	 *   - PathVariable(url) : id(수정할 번호)
	 *   - RequestBody(json) : review (수정할 정보)
	 * 리턴 : 수정 결과(ResponseEntity)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable int id, @RequestBody Review review) {
    	review.setReviewId(id);
        int result = service.updateReview(review);
        if (result == 1) return ResponseEntity.ok(id);
        else return ResponseEntity.badRequest().body("리뷰 수정 실패 : 없는 리뷰입니다.");
    }  
    /*
     * 2-3. 리뷰 삭제
     * 메서드 : DELETE
     * 엔드포인트 : /reviews/{id}
     * 파라미터
     *   - PathVariable(url) : id(삭제할 번호)
     *   - RequestBody : 없음
     * 리턴 : 삭제 결과(ResponseEntity)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable int id) {
		int result = service.deleteReview(id);
		if (result == 1) return ResponseEntity.ok("리뷰가 삭제되었습니다!");
        else return ResponseEntity.badRequest().body("리뷰 삭제 실패 : 없는 리뷰입니다.");
    }
    
    
    
    
	// 3. 좋아요
	/* 3-1. 좋아요 등록
	 * 
	 *  메서드 : POST
	 *  엔드포인트 : /reviews/like
	 *  파라미터
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : ReviewLikes(userId, reviewId)
	 *  리턴 : 없음
	 */
	@PostMapping("/like")
	public void likeOn(@RequestBody ReviewLikes like) {
		service.likeOn(like);
	}
	
	/* 3-2. 좋아요 해제
	 *
	 * 메서드 : DELETE
	 * 엔드포인트 : /reviews/like/{reviewId}
	 * 파라미터
	 *   - PathVariable(reviewId) : 좋아요 취소할 리뷰 번호
	 *   - RequestParam(userId) : 취소 요청 사용자 번호
	 * 리턴 : 없음
	 */
	@DeleteMapping("/like/{reviewId}")
	public void likeOff(@PathVariable int reviewId, @RequestParam int userId) {
		ReviewLikes like = new ReviewLikes(userId, reviewId);
		service.likeOff(like);
	}

}
