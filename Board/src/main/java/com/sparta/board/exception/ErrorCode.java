package com.sparta.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //예외 상황에 따른 에러 코드
    DUPLICATE_USER(400,"중복된 username입니다."),
    INVALID_TOKEN(400,"토큰이 유효하지 않습니다."),
    INVALID_USER(400, "작성자만 수정/삭제할 수 있습니다."),
    INVALID_LOGIN(400, "회원을 찾을 수 없습니다."),
    INVALID_ADMIN(400, "관리자 암호가 틀려 등록이 불가능합니다."),
    INVALID_PASSWORD(400, "비밀번호가 일치하지 않습니다."),
    NULL_BOARD(400, "해당 글이 없습니다."),
    NULL_TOKEN(400,"토큰이 없습니다.");


    private final int statusCode;
    private final String message;

}
