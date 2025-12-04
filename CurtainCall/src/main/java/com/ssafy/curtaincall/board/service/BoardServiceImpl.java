package com.ssafy.curtaincall.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.board.dto.Board;
import com.ssafy.curtaincall.board.dto.BoardLikes;
import com.ssafy.curtaincall.board.dto.BoardSearchCondition;
import com.ssafy.curtaincall.board.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardMapper mapper;
	
	//전체 목록 조회
	@Override
	public List<Board> getlist() {
		return mapper.selectAllBoard();
	}
	// 개별 항목 조회

	@Override
	public Board getBoard(int id) {
		return mapper.selectBoard(id);
	}
	
	// 조건별 조회
	@Override
	public List<Board> getBoardByCondition(BoardSearchCondition condition) {
		return mapper.selectBoardByCondition(condition);
	}
	
	//CUD
	//create
	@Override
	public int createBoard(Board board) {
		int result = mapper.insertBoard(board);
		if (result == 1) return board.getBoardId(); // int 값 들어옴
		return -1;		
	}
	//수정
	@Override
	public int updateBoard(Board board) {
		return mapper.updateBoard(board);
	}
	
	//삭제
	@Override
	public int deleteBoard(int id) {
		return mapper.deleteBoard(id);
	}

	////좋아요
	
	// 좋아요 등록 : 좋아요 확인 후 실행
	@Override
	public void likeOn(BoardLikes like) {
		System.out.println("좋아요 요청 - 이미 있는 좋아요인가? : " + mapper.checkLike(like));
		if (mapper.checkLike(like) == 0) mapper.insertLike(like);		
		else System.out.println("이미 있는 좋아요입니다.");
	}
	
	//좋아요 해제 : 좋아요 확인 후 실행
	@Override
	public void likeOff(BoardLikes like) {
		System.out.println("좋아요 삭제 요청 - 이미 있는 좋아요인가? : " + mapper.checkLike(like));
		if (mapper.checkLike(like) >= 1) mapper.deleteLike(like);	
		else System.out.println("삭제할 좋아요가 없습니다.");
	}


}
