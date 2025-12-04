package com.ssafy.curtaincall.reply.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.reply.dto.Reply;
import com.ssafy.curtaincall.reply.mapper.ReplyMapper;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
    ReplyMapper mapper;
	
	@Override
	public List<Reply> getlist() {
		return mapper.selectAllReply();
	}

	@Override
	public int createComment(Reply reply) {
		 return mapper.insertReply(reply);
	}

	@Override
	public int updateComment(Reply reply) {
		return mapper.updateReply(reply);
	}

	@Override
	public int deleteReply(int id) {
		return mapper.deleteReply(id);
	}

}
