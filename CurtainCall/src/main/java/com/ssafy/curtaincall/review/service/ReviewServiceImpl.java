package com.ssafy.curtaincall.review.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public List<MyReviewDto> getReviewByCondition(ReviewSearchCondition condition) {
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
		int result = mapper.insertReview(review);
		return result;	
	}
	//수정
	@Override
	public int updateReview(Review review) {
		// 이 유저가 이 뮤지컬에 대한 리뷰가 있으면 진행
		return mapper.updateReview(review);
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

	//// 좋아요
	@Override
	@Transactional
	public boolean toggleLike(int userId, int reviewId) {

	    int count = mapper.existsLike(userId, reviewId);

        if (count > 0) {
        	mapper.deleteLike(userId, reviewId); // 눌렀으면 취소
            return false;
            
        } else {
        	mapper.insertLike(userId, reviewId); // 안 눌렀으면 추가
            return true;
        }
	}

	@Override
	public boolean isLiked(int userId, int reviewId) {
		return mapper.existsLike(userId, reviewId) > 0;
	}

	@Override
	public int getLike(int reviewId) {
		return mapper.selectLike(reviewId);
	}
	
	

	@Override
	public List<MyReviewDto> getMyReview(int id) {
		return mapper.selectMyReview(id);
	}

	@Override
	public List<MyReviewDto> getTopReview() {
		return mapper.selectTopReview();
	}

	@Override
	public MyReviewDto getReview(int reviewId) {
		return mapper.selectReview(reviewId);
	}


}
