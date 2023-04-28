package com.sparta.board.exception;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter @Builder
public class ErrorResponse {
    private final String message;
    private final int statusCode;
    
    public static ResponseEntity<ErrorResponse> responseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .message(errorCode.getMessage())
                        .statusCode(errorCode.getHttpStatus().value())
                        .build()
                );
    }
}
