package com.ssafy.curtaincall.review.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.review.dto.Review;
import com.ssafy.curtaincall.review.dto.ReviewLikes;
import com.ssafy.curtaincall.review.dto.ReviewSearchCondition;
import com.ssafy.curtaincall.review.mapper.ReviewMapper;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	ReviewMapper mapper;
	
	//전체 목록 조회
	@Override
	public List<Review> getlist() {
		return mapper.selectAllReview();
	}
	// 개별 항목 조회

	@Override
	public Review getReview(int id) {
		return mapper.selectReview(id);
	}
	
	// 조건별 조회
	@Override
	public List<Review> getReviewByCondition(ReviewSearchCondition condition) {
		return mapper.selectReviewByCondition(condition);
	}
	
	//CUD
	//create
	@Override
	public int createReview(Review review) {
		int result = mapper.insertReview(review);
		if (result == 1) return review.getReviewId(); // int 값 들어옴
		return -1;		
	}
	//수정
	@Override
	public int updateReview(Review review) {
		return mapper.updateReview(review);
	}
	
	//삭제
	@Override
	public int deleteReview(int id) {
		return mapper.deleteReview(id);
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


}
