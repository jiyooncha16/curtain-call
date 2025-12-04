package com.ssafy.curtaincall.review.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.curtaincall.review.dto.Review;
import com.ssafy.curtaincall.review.dto.ReviewLikes;
import com.ssafy.curtaincall.review.dto.ReviewSearchCondition;

@Mapper
public interface ReviewMapper {
	
	//조회
	public List<Review> selectAllReview(); // 전체 목록
	public List<Review> selectReviewByCondition(ReviewSearchCondition condition); // 조건부 목록(검색)
	public Review selectReview(int id); // 상세조회
	
	//CUD
    int insertReview(Review review);
    int updateReview(Review review);
    int deleteReview(int reviewId);
	
	//좋아요
	public int checkLike(ReviewLikes like); // 좋아요 찍혀있는지 확인 (개수 반환)
	public void insertLike(ReviewLikes like); // 좋아요 등록
	public void deleteLike(ReviewLikes like); // 좋아요 해제
	
}
