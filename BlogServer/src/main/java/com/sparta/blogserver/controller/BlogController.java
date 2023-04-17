package com.sparta.blogserver.controller;

import com.sparta.blogserver.Service.PostingService;
import com.sparta.blogserver.dto.PostingRequestDto;
import com.sparta.blogserver.dto.PostingResponseDto;
import com.sparta.blogserver.entity.Blog;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogController {

    private final PostingService postingService;


    // 메인 페이지. 전체 게시글 목록 조회
    @GetMapping("/")
    public List<Blog> getPostingList() {
        return postingService.getPostingList();
    }

    // 게시글 작성
    @PostMapping("/create")
    public Blog createPosting(@RequestBody PostingRequestDto requestDto) {
        return postingService.createPosting(requestDto);
    }

    // 선택한 게시글 조회
    @GetMapping("/posting/{id}")
    public PostingResponseDto getPosting(@PathVariable Long id) {
        return postingService.getPosting(id);
    }

    // 선택한 게시글 수정
    @PutMapping("/update/{id}")
    public Long updatePosting(@PathVariable Long id, @RequestBody PostingRequestDto requestDto) {
        return postingService.updatePosting(id, requestDto);
    }

    // 선택한 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public Long deletePosting(@PathVariable Long id, @RequestBody PostingRequestDto requestDto) {
        return postingService.deletePosting(id, requestDto);
    }


}
