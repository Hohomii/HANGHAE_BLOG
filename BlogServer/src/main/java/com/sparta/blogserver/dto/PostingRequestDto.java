package com.sparta.blogserver.dto;

import lombok.Getter;

@Getter
public class PostingRequestDto {
    private String title;
    private String writer;
    private String content;
    private String password;

}
