package com.sparta.board.controller;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.dto.MsgResponseDto;
import com.sparta.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/board/{id}/comment")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(id, requestDto, request);
    }

    @PutMapping("/board/{id}/comment/{cmtId}")
    public CommentResponseDto updateComment(@PathVariable Long id, @PathVariable Long cmtId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, cmtId, requestDto, request);
    }

//    @DeleteMapping("/board/{id}/comment/{id}")
//    public ResponseEntity<MsgResponseDto> deleteComment(@PathVariable Long id, Long id2, @RequestBody HttpServletRequest request) {
//        commentService.deleteComment(id, id2, request);
//        return ResponseEntity.ok(new MsgResponseDto("댓글 삭제 성공!", HttpStatus.OK.value()));
//    }

}
