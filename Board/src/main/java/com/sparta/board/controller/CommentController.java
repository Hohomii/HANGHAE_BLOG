package com.sparta.board.controller;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.dto.MsgResponseDto;
import com.sparta.board.security.UserDetailsImpl;
import com.sparta.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(requestDto, userDetails.getUser());
    }

    @PutMapping("/comment/{cmtId}")
    public CommentResponseDto updateComment(@PathVariable Long cmtId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(cmtId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/comment/{cmtId}")
    public ResponseEntity<MsgResponseDto> deleteComment(@PathVariable Long cmtId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(cmtId, userDetails.getUser());
        return ResponseEntity.ok(new MsgResponseDto("댓글 삭제 성공!", HttpStatus.OK.value()));
    }

    @PostMapping("/comment/{cmtId}/like")
    public ResponseEntity<MsgResponseDto> likeComment(@PathVariable Long cmtId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean likeResult = commentService.likeComment(cmtId, userDetails.getUser());
        if (likeResult) {
            return ResponseEntity.ok(new MsgResponseDto("좋아요 성공!", HttpStatus.OK.value()));
        } else {
            return ResponseEntity.ok(new MsgResponseDto("좋아요 취소 성공!", HttpStatus.OK.value()));
        }
    }

}
