package com.ssafy.curtaincall.reply.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.curtaincall.reply.dto.Reply;
import com.ssafy.curtaincall.reply.dto.ReplyResponse;

@Mapper
public interface ReplyMapper {
	
	//CRUD
	List<ReplyResponse> selectAllReply(int boardId);
    int insertReply(Reply reply);
    int updateReply(Reply reply);
    int deleteReply(int replyId);


	
}
