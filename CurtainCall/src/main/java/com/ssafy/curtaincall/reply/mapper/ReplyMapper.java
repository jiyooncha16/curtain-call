package com.ssafy.curtaincall.reply.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.curtaincall.reply.dto.Reply;

@Mapper
public interface ReplyMapper {
	
	//CRUD
    List<Reply> selectAllReply();
    int insertReply(Reply reply);
    int updateReply(Reply reply);
    int deleteReply(int replyId);


	
}
