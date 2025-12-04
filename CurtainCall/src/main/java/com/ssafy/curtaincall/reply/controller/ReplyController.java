package com.ssafy.curtaincall.reply.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.curtaincall.reply.dto.Reply;
import com.ssafy.curtaincall.reply.service.ReplyService;

@RestController
@RequestMapping("/reply")
@CrossOrigin("*")
public class ReplyController {
	
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
	@Qualifier("replyServiceImpl")
	ReplyService service;
	

	// 1. CRUD
	/* 1-1. 전체 게시글 목록 조회 - 테스트 완료
	 *
	 * 메서드 : GET
	 * 엔드포인트 : /reply
	 * 파라미터
	 *   - pathVariable(url) : 없음
	 *   - RequestBody(json) : 없음
	 * 리턴 : List<Reply> 게시글 전체 목록
	 */
	@GetMapping("")
	public ResponseEntity<List<Reply>> getlist() {
		List<Reply> list = service.getlist();
		if (list == null || list.size() == 0) return ResponseEntity.noContent().build();
		return ResponseEntity.ok(list);
	}
	/* 1-2. 게시글 등록 
	 * 메서드 : POST
	 * 엔드포인트 : /reply
	 * 파라미터
	 *   - RequestBody(json) : Reply
	 * 리턴 : 게시글 생성 결과 메시지(ResponseEntity)
	 */
	@PostMapping("")
    public ResponseEntity<?> create(@RequestBody Reply reply) {
        int result = service.createComment(reply);
        if (result == 1) return ResponseEntity.ok("게시글이 생성되었습니다.");
        else return ResponseEntity.badRequest().body("게시글 생성 실패");
    }
	

	 /*
	 * 1-3. 댓글 수정
	 * 메서드 : PUT
	 * 엔드포인트 : /reply/{id}
	 * 파라미터
	 *   - PathVariable(url) : id(수정할 게시글 번호)
	 *   - RequestBody(json) : commet (수정할 게시글 정보)
	 * 리턴 : 수정 결과(ResponseEntity)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Reply updatedReply) {
        updatedReply.setReplyId(id);
        int result = service.updateComment(updatedReply);
        if (result == 1) return ResponseEntity.ok("게시글이 수정되었습니다!");
        else return ResponseEntity.badRequest().body("게시글 수정 실패");
    }  
    /*
     * 1-4. 댓글 삭제
     * 메서드 : DELETE
     * 엔드포인트 : /reply/{id}
     * 파라미터
     *   - PathVariable(url) : id(삭제할 댓글 번호)
     *   - RequestBody : 없음
     * 리턴 : 삭제 결과(ResponseEntity)
     */
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
		int result = service.deleteReply(id);
		if (result == 1) return ResponseEntity.ok("게시글이 삭제되었습니다!");
        else return ResponseEntity.badRequest().body("게시글 삭제 실패");
    }


}
