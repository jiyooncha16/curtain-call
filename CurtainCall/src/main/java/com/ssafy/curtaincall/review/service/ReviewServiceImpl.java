package com.ssafy.curtaincall.review.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.review.dto.MyReviewDto;
import com.ssafy.curtaincall.review.dto.Review;
import com.ssafy.curtaincall.review.dto.ReviewLikes;
import com.ssafy.curtaincall.review.dto.ReviewRateDto;
import com.ssafy.curtaincall.review.dto.ReviewSearchCondition;
import com.ssafy.curtaincall.review.mapper.ReviewMapper;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	ReviewMapper mapper;
	
//	//전체 목록 조회
//	@Override
//	public List<Review> getlist() {
//		return mapper.selectAllReview();
//	}
	
	// 평점 평균 조회
	@Override
	public double getReviewRate(int id) {
		return mapper.selectReviewRate(id);
	}
	
	// 조건별 조회
	@Override
	public List<Review> getReviewByCondition(ReviewSearchCondition condition) {
		return mapper.selectReviewByCondition(condition);
	}


	@Override
	public List<ReviewRateDto> getReviewRateStats(int id) {
		return mapper.selectReviewRateStats(id);
	}
	
	//CUD
	//create
	@Override
	public int createReview(Review review) {
		// 이 유저가 이 뮤지컬에 대한 리뷰를 작성한적이 없어야 진행
		if(mapper.selectReview(review) == null) {
			int result = mapper.insertReview(review);
			if (result == 1) return review.getReviewId(); // int 값 들어옴
		}
		return -1;		
	}
	//수정
	@Override
	public int updateReview(Review review) {
		// 이 유저가 이 뮤지컬에 대한 리뷰가 있으면 진행
		if(mapper.selectReview(review) != null) {
			return mapper.updateReview(review);
		}
		return -1;
	}
	
	//삭제
	@Override
	public int deleteReview(int id) {

		// 이 유저가 이 뮤지컬에 대한 리뷰가 있으면 진행
		if(mapper.selectReviewById(id) != null) {
			return mapper.deleteReview(id);
		}
		return -1;
	}

	////좋아요
	
	// 좋아요 등록 : 좋아요 확인 후 실행
	@Override
	public void likeOn(ReviewLikes like) {
		System.out.println("좋아요 요청 - 이미 있는 좋아요인가? : " + mapper.checkLike(like));
		if (mapper.checkLike(like) == 0) mapper.insertLike(like);		
		else System.out.println("이미 있는 좋아요입니다.");
	}
	
	//좋아요 해제 : 좋아요 확인 후 실행
	@Override
	public void likeOff(ReviewLikes like) {
		System.out.println("좋아요 삭제 요청 - 이미 있는 좋아요인가? : " + mapper.checkLike(like));
		if (mapper.checkLike(like) >= 1) mapper.deleteLike(like);	
		else System.out.println("삭제할 좋아요가 없습니다.");
	}

	@Override
	public List<MyReviewDto> getMyReview(int id) {
		return mapper.selectMyReview(id);
	}

	@Override
	public List<MyReviewDto> getTopReview() {
		return mapper.selectTopReview();
	}


}
