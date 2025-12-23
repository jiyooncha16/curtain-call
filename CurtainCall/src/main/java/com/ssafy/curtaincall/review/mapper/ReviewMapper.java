package com.ssafy.curtaincall.review.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.curtaincall.review.dto.MyReviewDto;
import com.ssafy.curtaincall.review.dto.Review;
import com.ssafy.curtaincall.review.dto.ReviewLikes;
import com.ssafy.curtaincall.review.dto.ReviewRateDto;
import com.ssafy.curtaincall.review.dto.ReviewSearchCondition;

@Mapper
public interface ReviewMapper {
	
	//조회
//	public List<Review> selectAllReview(); // 전체 목록
	public List<MyReviewDto> selectReviewByCondition(ReviewSearchCondition condition); // 조건부 목록(검색)
	public double selectReviewRate(int musicalId); // 평점 평균 조회
	public MyReviewDto selectReview(int reviewId);
	public MyReviewDto selectReviewById(int id);
	public List<ReviewRateDto> selectReviewRateStats(int id);
	public List<MyReviewDto> selectMyReview(int id);
	public List<MyReviewDto> selectTopReview();
	
	//CUD
    int insertReview(Review review);
    int updateReview(Review review);
    int deleteReview(int reviewId);
	
	//좋아요
    public int selectLike(int reviewId); // 이 뮤지컬의 좋아요 개수
	public int existsLike(@Param("userId")int userId, @Param("reviewId")int reviewId); // 내 좋아요
	public void deleteLike(@Param("userId")int userId, @Param("reviewId")int reviewId);
	public void insertLike(@Param("userId")int userId, @Param("reviewId")int reviewId);

}
