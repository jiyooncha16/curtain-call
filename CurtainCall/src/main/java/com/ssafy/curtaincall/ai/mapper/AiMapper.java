package com.ssafy.curtaincall.ai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.curtaincall.ai.dto.HashtagDTO;

@Mapper
public interface AiMapper {

	public List<HashtagDTO> selectCountGroupByHashtag(int id);

}
