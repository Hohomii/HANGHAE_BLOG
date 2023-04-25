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

    @PostMapping("/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    @PutMapping("/comment/{cmtId}")
    public CommentResponseDto updateComment(@PathVariable Long cmtId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(cmtId, requestDto, request);
    }

    @DeleteMapping("/comment/{cmtId}")
    public ResponseEntity<MsgResponseDto> deleteComment(@PathVariable Long cmtId, HttpServletRequest request) {
        commentService.deleteComment(cmtId, request);
        return ResponseEntity.ok(new MsgResponseDto("댓글 삭제 성공!", HttpStatus.OK.value()));
    }

}
