package com.sparta.board.exception;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter @Builder
public class ErrorResponse {
    private final String message;
    private final int statusCode;
    
    public static ResponseEntity<ErrorResponse> responseEntity(StatusCode statusCode) {
        return ResponseEntity
                .status(statusCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .message(statusCode.getMessage())
                        .statusCode(statusCode.getHttpStatus().value())
                        .build()
                );
    }
}
