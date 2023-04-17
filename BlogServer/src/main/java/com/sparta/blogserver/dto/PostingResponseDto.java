package com.sparta.blogserver.dto;

import com.sparta.blogserver.entity.Blog;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostingResponseDto {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private String password;

    public PostingResponseDto(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.writer = blog.getWriter();
        this.content = blog.getContent();
        this.password = blog.getPassword();
    }

}
