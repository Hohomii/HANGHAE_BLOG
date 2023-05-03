package com.sparta.board.controller;

import com.sparta.board.dto.ApiResponseDto;
import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.security.UserDetailsImpl;
import com.sparta.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.board.exception.StatusCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;


    //전체 게시글+댓글 조회
    @GetMapping("/board")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }

    //선택 게시글 조회
    @GetMapping("/board/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    //게시글 작성
    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        BoardRequestDto boardRequestDto = new BoardRequestDto(requestDto.getTitle(), requestDto.getContent());
        return boardService.createBoard(boardRequestDto);
    }

    //게시글 수정
    @PutMapping("/board/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updateBoard(id, requestDto, userDetails.getUser());
    }

    //게시글 삭제
    @DeleteMapping("/board/{id}")
    public ApiResponseDto deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(id, userDetails.getUser());
        return new ApiResponseDto(BOARD_DELETE_OK);
    }

    //게시글 좋아요 or 좋아요 취소
    @PostMapping("/board/{id}/like")
    public ApiResponseDto likeBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean likeResult = boardService.likeBoard(id, userDetails.getUser());
        if (likeResult) {
            return new ApiResponseDto(LIKE_OK);
        }
        return new ApiResponseDto(UNLIKE_OK);
    }
}

