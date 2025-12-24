package com.ssafy.curtaincall.ai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ssafy.curtaincall.ai.dto.HashtagDTO;
import com.ssafy.curtaincall.ai.mapper.AiMapper;

public interface AiService {
	public List<HashtagDTO> hashtagCount(int id);
	public String generateUserTaste(int id);
	public String generateReview(String title, String keyword);
}
