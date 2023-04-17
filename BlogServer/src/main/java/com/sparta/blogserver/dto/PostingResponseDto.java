package com.sparta.blogserver.dto;

import com.sparta.blogserver.entity.Blog;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
// 응답값에는 id(PK),pw는 보낼 필요 없음
public class PostingResponseDto {
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createdAt; // 작성일시 보내는 방법!

    public PostingResponseDto(Blog blog) {
        this.title = blog.getTitle();
        this.writer = blog.getWriter();
        this.content = blog.getContent();
        this.createdAt = blog.getCreatedAt();
    }

}
