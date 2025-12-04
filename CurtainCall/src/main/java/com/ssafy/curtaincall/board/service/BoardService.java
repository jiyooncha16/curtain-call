package com.ssafy.curtaincall.board.service;

import java.util.List;

import com.ssafy.curtaincall.board.dto.Board;
import com.ssafy.curtaincall.board.dto.BoardLikes;
import com.ssafy.curtaincall.board.dto.BoardSearchCondition;

public interface BoardService {

	/*
	 * 서비스 인터페이스
	 * - 주석 작성할 것
	 * 
	 */
	
	
	
	//조회
	List<Board> getlist(); // 전체 뮤지컬 리스트 반환
	Board getBoard(int id); // 게시글 상세조회
	List<Board> getBoardByCondition(BoardSearchCondition condition); // 조건부 리스트 반환

	//CUD
    int createBoard(Board board);             // 게시글 등록 
    int updateBoard(Board board);             // 게시글 수정
    int deleteBoard(int id);                  // 게시글 삭제
	
	//좋아요
	void likeOn(BoardLikes like); // 좋아요 등록
	void likeOff(BoardLikes like); // 좋아요 해제
	

}
