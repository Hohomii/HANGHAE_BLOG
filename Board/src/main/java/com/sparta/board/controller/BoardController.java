package com.sparta.board.controller;
import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.MsgResponseDto;
import com.sparta.board.repository.BoardLikeRepository;
import com.sparta.board.security.UserDetailsImpl;
import com.sparta.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;
    private final BoardLikeRepository boardLikeRepository;


    // 메인 페이지. 전체 게시글+댓글 목록 조회(제목, 작성자명, 작성 내용, 작성 날짜)
    @GetMapping("/board")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }

    @GetMapping("/board/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    // 게시글 작성 : http헤더에 토큰이 있어야만 작성 가능
    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        if (userDetails == null) {
//            throw new CustomException(ErrorCode.NULL_TOKEN);
//        }
        return boardService.createBoard(requestDto, userDetails.getUser());
    }

    @PutMapping("/board/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updateBoard(id, requestDto, userDetails.getUser());
    }

    // 선택한 게시글 삭제
    @DeleteMapping("/board/{id}")
    public ResponseEntity<MsgResponseDto> deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(id, userDetails.getUser());
        return ResponseEntity.ok(new MsgResponseDto("글 삭제 성공!", HttpStatus.OK.value()));
    }

    @PostMapping("/board/{id}/like")
    public ResponseEntity<MsgResponseDto> likeBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean likeResult = boardService.likeBoard(id, userDetails.getUser());
        if (likeResult) {
            return ResponseEntity.ok(new MsgResponseDto("좋아요 성공!", HttpStatus.OK.value()));
        }
        return ResponseEntity.ok(new MsgResponseDto("좋아요 취소 성공!", HttpStatus.OK.value()));
    }
}
