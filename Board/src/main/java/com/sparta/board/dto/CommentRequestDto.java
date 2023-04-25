package com.sparta.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CommentRequestDto {
    private Long boardId;
    private String content;
}
