package com.ssafy.curtaincall.reply.service;

import java.util.List;

import com.ssafy.curtaincall.reply.dto.Reply;

public interface ReplyService {
	List<Reply> getlist();
    int createComment(Reply reply);
    int updateComment(Reply reply);
    int deleteReply(int id);
}
