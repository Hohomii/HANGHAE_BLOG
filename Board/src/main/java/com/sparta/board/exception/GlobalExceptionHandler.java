package com.sparta.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice //전역 예외 처리
//추상 클래스 ResponseEntityExceptionHandle 이거 쓰면 무엇이 좋은지 공부
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //중복 회원
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<?> handleDuplicateUserException(DuplicateUserException e) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_USER;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage(), errorCode.getStatusCode());
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    //유효하지 않은 토큰
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(InvalidTokenException e) {
        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage(), errorCode.getStatusCode());
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    //로그인 에러 : id,pw 틀림
    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<?> handleInvalidLoginException(InvalidLoginException e) {
        ErrorCode errorCode = ErrorCode.INVALID_LOGIN;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage(), errorCode.getStatusCode());
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    //작성자만 수정/삭제 가능
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<?> handleInvalidUserException(InvalidUserException e) {
        ErrorCode errorCode = ErrorCode.INVALID_USER;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage(), errorCode.getStatusCode());
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    //비밀번호 불일치
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> handleInvalidPasswordException(InvalidPasswordException e) {
        ErrorCode errorCode = ErrorCode.INVALID_PASSWORD;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage(), errorCode.getStatusCode());
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    //관리자 권한 불일치
    @ExceptionHandler(InvalidAdminException.class)
    public ResponseEntity<?> handleInvalidAdminException(InvalidAdminException e) {
        ErrorCode errorCode = ErrorCode.INVALID_ADMIN;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage(), errorCode.getStatusCode());
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    //해당 글 없음
    @ExceptionHandler(NullBoardException.class)
    public ResponseEntity<?> handleNullBoardException(NullBoardException e) {
        ErrorCode errorCode = ErrorCode.NULL_BOARD;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage(), errorCode.getStatusCode());
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    //토큰 없음
    @ExceptionHandler(NullTokenException.class)
    public ResponseEntity<?> handleNullTokenException(NullTokenException e) {
        ErrorCode errorCode = ErrorCode.NULL_TOKEN;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage(), errorCode.getStatusCode());
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    @Getter
    @AllArgsConstructor
    private static class ErrorResponse {
        private String message;
        private int statusCode;
    }

    public static class DuplicateUserException extends RuntimeException {
        public DuplicateUserException() {
            super();

        }
    }

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException() {
            super();
        }
    }

    public static class InvalidLoginException extends RuntimeException {
        public InvalidLoginException() {
            super();
        }
    }

    public static class InvalidUserException extends RuntimeException {
        public InvalidUserException() {
            super();
        }
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException() {
            super();
        }
    }

    public static class InvalidAdminException extends RuntimeException {
        public InvalidAdminException() {
            super();
        }
    }

    public static class NullBoardException extends RuntimeException {
        public NullBoardException() {
            super();
        }
    }

    public static class NullTokenException extends RuntimeException {
        public NullTokenException() {
            super();
        }
    }
}
