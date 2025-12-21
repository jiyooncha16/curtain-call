package com.ssafy.curtaincall.review.service;

import java.util.List;

import com.ssafy.curtaincall.board.dto.Board;
import com.ssafy.curtaincall.board.dto.BoardLikes;
import com.ssafy.curtaincall.board.dto.BoardSearchCondition;
import com.ssafy.curtaincall.review.dto.MyReviewDto;
import com.ssafy.curtaincall.review.dto.Review;
import com.ssafy.curtaincall.review.dto.ReviewLikes;
import com.ssafy.curtaincall.review.dto.ReviewRateDto;
import com.ssafy.curtaincall.review.dto.ReviewSearchCondition;

public interface ReviewService {

	/*
	 * 서비스 인터페이스
	 * - 주석 작성할 것
	 * 
	 */
	
	
	//조회
//	List<Review> getlist(); // 전체 뮤지컬 리스트 반환
	double getReviewRate(int id); // 평점 평균 조회
	List<Review> getReviewByCondition(ReviewSearchCondition condition); // 조건부 리스트 반환
	List<ReviewRateDto> getReviewRateStats(int id); //평점 통계
	List<MyReviewDto> getMyReview(int id); // 내 리뷰
	List<MyReviewDto> getTopReview();
	
	//CUD
    int createReview(Review review);             // 등록 
    int updateReview(Review review);             // 수정
    int deleteReview(int id);                  // 삭제
	
	//좋아요
	void likeOn(ReviewLikes like); // 좋아요 등록
	void likeOff(ReviewLikes like); // 좋아요 해제
	

}
