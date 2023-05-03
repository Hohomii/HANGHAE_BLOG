package com.sparta.board.controller;

import com.sparta.board.dto.ApiResponseDto;
import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.security.UserDetailsImpl;
import com.sparta.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.board.exception.StatusCode.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto) {
        CommentRequestDto commentRequestDto = new CommentRequestDto(requestDto.getBoardId(), requestDto.getContent());
        return commentService.createComment(commentRequestDto);
    }

    //댓글 수정
    @PutMapping("/comment/{cmtId}")
    public CommentResponseDto updateComment(@PathVariable Long cmtId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(cmtId, requestDto, userDetails.getUser());
    }

    //댓글 삭제
    @DeleteMapping("/comment/{cmtId}")
    public ApiResponseDto deleteComment(@PathVariable Long cmtId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(cmtId, userDetails.getUser());
        return new ApiResponseDto(COMMENT_DELETE_OK);
    }

    //댓글 좋아요 or 좋아요 취소
    @PostMapping("/comment/{cmtId}/like")
    public ApiResponseDto likeComment(@PathVariable Long cmtId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean likeResult = commentService.likeComment(cmtId, userDetails.getUser());
        if (likeResult) {
            return new ApiResponseDto(LIKE_OK);
        } else {
            return new ApiResponseDto(UNLIKE_OK);
        }
    }
}
