package com.ssafy.curtaincall.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.curtaincall.board.dto.Board;
import com.ssafy.curtaincall.board.dto.BoardLikes;
import com.ssafy.curtaincall.board.dto.BoardSearchCondition;
import com.ssafy.curtaincall.board.service.BoardService;

@RestController
@RequestMapping("/boards")
@CrossOrigin("*")
public class BoardController {
	
	/*
	 * 서비스 의존성 주입 필수 (@Autowired)
	 * 각 항목 정리해서 주석 상세하게 달기
	 * - 번호 정리
	 * - 각 항목의 파라미터와 리턴 값
	 * 
	 * RESTful 하게 작성 (get, post, put, delete)
	 * url에 주의할 것 (단, http메서드가 다르면 url은 겹쳐도 됨)
	 * 
	 * 리턴은 결과값 고려(수정됐는지, 리스트가 비었는지 등)하여
	 * ResponseEntity로 반환
	 * 
	 * 200(ok) : ResponseEntity.ok()
	 * 200(ok) + 리턴값 : ResponseEntity.ok(list)
	 * 
	 * 401(noContent) : ResponseEntity.noContent()
	 * 401(noContent) + 메시지 : ResponseEntity.status(HttpStatus.noContent).body("내용이 없습니다. (401)")
	 * 
	 * 404(notFound) : ResponseEntity.notFound()
	 * 404(notFound) + 메시지 : ResponseEntity.status(HttpStatus.notFound).body("요청하신 내용을 찾지 못했습니다. (404)")
	 */
	
	@Autowired
	@Qualifier("boardServiceImpl")

	BoardService service;
	
	// 1. 조회
	/* 1-1. 전체 게시글 목록 조회 - 테스트 완료
	 *
	 * 메서드 : GET
	 * 엔드포인트 : /boards
	 * 파라미터
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : 없음
	 * 리턴 : List<Board> 게시글 전체 목록
	 */
	@GetMapping("")
	public ResponseEntity<List<Board>> getlist() {
		List<Board> list = service.getlist();
		if (list == null || list.size() == 0) return ResponseEntity.noContent().build();
		return ResponseEntity.ok(list);
	}
	
	/* 1-2. 게시글 상세 조회 - 테스트 완료
	 *
	 * 메서드 : GET
	 * 엔드포인트 : /boards/{id}
	 * 파라미터
	 *   - PathVariable(id) : 조회할 게시글 번호
	 *   - RequestBody(json) : 없음
	 * 리턴 : Board 게시글 상세 정보
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Board> getBoard(@PathVariable int id) {
		Board board = service.getBoard(id);
		if (board == null) return ResponseEntity.noContent().build();
		else return ResponseEntity.ok(board);
	}
	
	/* 1-3. 필터링 조회 (검색) : 테스트 완료
	 * 
	 *  메서드 : GET
	 *  엔드포인트 : /boards/{id}
	 *  파라미터
	 *   - ModelAttribute : SearchCondition
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : 없음
	 *  리턴 : List<Board> 게시글 전체 목록
	 *  
	 *  세부내용
	 *  - 필터링 : 해시태그별(O), 키워드별(O), 극장별(O), 날짜별
	 *  - 순서 : 좋아요(O), 랜덤
	 *  - 페이징 : 몇페이지, 몇개씩
	 *  
	 *  사용처
	 *  - 검색
	 *  - 메인페이지(공연중 작품, 개막예정 작품, 추천리스트, 랜덤리스트, 핫랭킹 작품)
	 */
	@GetMapping("/search")
	public ResponseEntity<List<Board>> getMusicalByCondition(@ModelAttribute BoardSearchCondition condition) {
		condition.setOffset(condition.getPage() * condition.getSize());
		List<Board> list = service.getBoardByCondition(condition);
		if (list == null || list.size() == 0) return ResponseEntity.noContent().build();
		else return ResponseEntity.ok(list);
	}
	// 2. CUD 
	/* 2-1. 게시글 등록 
	 * 메서드 : POST
	 * 엔드포인트 : /boards
	 * 파라미터
	 *   - RequestBody(json) : Board
	 * 리턴 : 게시글 생성 결과 메시지(ResponseEntity)
	 */
	@PostMapping("")
    public ResponseEntity<?> create(@RequestBody Board board) {
        int result = service.createBoard(board);
        if (result == 1) return ResponseEntity.ok("게시글이 생성되었습니다.");
        else return ResponseEntity.badRequest().body("게시글 생성 실패");
    }
	

	 /*
	 * 2-2. 게시글 수정
	 * 메서드 : PUT
	 * 엔드포인트 : /boards/{id}
	 * 파라미터
	 *   - PathVariable(url) : id(수정할 게시글 번호)
	 *   - RequestBody(json) : Board (수정할 게시글 정보)
	 * 리턴 : 수정 결과(ResponseEntity)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Board updatedBoard) {
        updatedBoard.setBoardId(id);
        int result = service.updateBoard(updatedBoard);
        if (result == 1) return ResponseEntity.ok("게시글이 수정되었습니다!");
        else return ResponseEntity.badRequest().body("게시글 수정 실패");
    }  
    /*
     * 2-3. 게시글 삭제
     * 메서드 : DELETE
     * 엔드포인트 : /boards/{id}
     * 파라미터
     *   - PathVariable(url) : id(삭제할 게시글 번호)
     *   - RequestBody : 없음
     * 리턴 : 삭제 결과(ResponseEntity)
     */
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
		int result = service.deleteBoard(id);
		if (result == 1) return ResponseEntity.ok("게시글이 삭제되었습니다!");
        else return ResponseEntity.badRequest().body("게시글 삭제 실패");
    }
	// 3. 좋아요
	/* 3-1. 좋아요 등록 - 테스트 완료
	 * 
	 *  메서드 : POST
	 *  엔드포인트 : /boards/like
	 *  파라미터
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : BoardLikes(userId,  boardId)
	 *  리턴 : 없음
	 */
	@PostMapping("/like")
	public void likeOn(@RequestBody BoardLikes like) {
		service.likeOn(like);
	}
//	@PostMapping("/like")
//	public ResponseEntity<?> likeOn(@RequestBody BoardLikes like) {
//	    boolean ok = service.likeOn(like); // boolean 또는 int 리턴으로 바꿈
//	    return ok ? ResponseEntity.status(HttpStatus.CREATED).build()
//	              : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("좋아요 등록 실패");
//	}
	
	/* 3-2. 좋아요 해제 - 테스트 완료
	 *
	 * 메서드 : DELETE
	 * 엔드포인트 : /boards/like/{boardId}
	 * 파라미터
	 *   - PathVariable(boardId) : 좋아요 취소할 게시글 번호
	 *   - RequestParam(userId) : 취소 요청 사용자 번호
	 * 리턴 : 없음
	 */
	@DeleteMapping("/like/{boardId}")
	public void likeOff(@PathVariable int boardId, @RequestParam int userId) {
		//빨간줄 무시 : 지금 명시적으로 생성자가 없어서 생기는 이슈. 롬복에서 생성자 만들어주니 괜찮음
		BoardLikes like = new BoardLikes(userId, boardId);
		service.likeOff(like);
	}

}
