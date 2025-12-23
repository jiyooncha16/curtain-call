package com.ssafy.curtaincall.reply.service;

import java.util.List;

import com.ssafy.curtaincall.reply.dto.Reply;
import com.ssafy.curtaincall.reply.dto.ReplyResponse;

public interface ReplyService {
	List<ReplyResponse> getlist(int boardId);
    int createComment(Reply reply);
    int updateComment(Reply reply);
    int deleteReply(int id);
}
