package com.sparta.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 BAD_REQUEST : 잘못된 요청
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"토큰이 유효하지 않습니다."),
    INVALID_USER(HttpStatus.BAD_REQUEST, "작성자만 수정/삭제할 수 있습니다."),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST,"중복된 username입니다."),
    INVALID_ADMIN(HttpStatus.BAD_REQUEST, "관리자 암호가 틀려 등록이 불가능합니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // 404 NOT_FOUND : Resource를 찾을 수 없음
    NULL_BOARD(HttpStatus.UNAUTHORIZED, "해당 글이 없습니다."),
    NULL_TOKEN(HttpStatus.UNAUTHORIZED,"토큰이 없습니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "회원을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
