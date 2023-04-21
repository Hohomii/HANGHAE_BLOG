package com.sparta.board.dto;

import com.sparta.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
// 응답값에는 id(PK),pw는 보낼 필요 없음
public class BoardResponseDto {
    private String title;
    private String username;
    private String content;
    private LocalDateTime modifiedAt; // 작성(수정)일시 보내는 방법!

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.content = board.getContent();
        this.modifiedAt = board.getModifiedAt();
    }

}
