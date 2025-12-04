package com.ssafy.curtaincall.video.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.curtaincall.video.dto.Video;

@Mapper
public interface VideoMapper {
	
	//조회
	public List<Video> selectAllVideo(); // 전체 목록
	public Video selectVideo(int id); // 뮤지컬 상세조회
	public List<Video> getVideoByViews(); // 전체 비디오 조회수 순 정렬
	
	
	
}
