package com.ssafy.curtaincall.video.service;

import java.util.List;

import com.ssafy.curtaincall.video.dto.Video;

public interface VideoService {

	//조회
	List<Video> getlist();//전체 비디오 리스트
	Video getVideo(int id);// -> 검색 가능하게 할까요?
    List<Video> getVideoByViews(); // 전체 비디오 조회수 순 정렬
}
