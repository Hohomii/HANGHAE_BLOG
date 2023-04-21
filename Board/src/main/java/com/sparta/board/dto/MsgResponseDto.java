package com.sparta.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 왜 이 어노테이션들이 다 필요한가? 이 dto가 쓰이는 상황이 어떤 상황이기에?
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MsgResponseDto {
    private String msg;
    private int StatusCode;
}
