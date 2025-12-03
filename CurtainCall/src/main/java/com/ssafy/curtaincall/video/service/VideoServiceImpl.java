package com.ssafy.curtaincall.video.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.video.dto.Video;
import com.ssafy.curtaincall.video.mapper.VideoMapper;

@Service
public class VideoServiceImpl implements VideoService {
	@Autowired
	VideoMapper mapper;

	@Override
	    public List<Video> getlist() {
	        return mapper.selectAllVideo();
	    }

	@Override
	    public Video getVideo(int id) {
	        return mapper.selectVideo(id);
	    }

	@Override
	    public List<Video> getVideoByViews() {
	        return mapper.getVideoByViews(); // mapper에서 ORDER BY view_cnt DESC
	    }

}
