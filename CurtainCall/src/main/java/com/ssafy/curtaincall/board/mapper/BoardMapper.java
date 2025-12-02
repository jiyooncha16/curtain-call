package com.ssafy.curtaincall.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.curtaincall.board.dto.Board;
import com.ssafy.curtaincall.board.dto.BoardLikes;
import com.ssafy.curtaincall.board.dto.BoardSearchCondition;

@Mapper
public interface BoardMapper {
	
	//조회
	public List<Board> selectAllBoard(); // 전체 목록
	public List<Board> selectBoardByCondition(BoardSearchCondition condition); // 조건부 목록(검색)
	public Board selectBoard(int id); // 게시글 상세조회
	
	//CUD
    int insertBoard(Board board);
    int updateBoard(Board board);
    int deleteBoard(int boardId);
	
	//좋아요
	public int checkLike(BoardLikes like); // 좋아요 찍혀있는지 확인 (개수 반환)
	public void insertLike(BoardLikes like); // 좋아요 등록
	public void deleteLike(BoardLikes like); // 좋아요 해제
	
}
