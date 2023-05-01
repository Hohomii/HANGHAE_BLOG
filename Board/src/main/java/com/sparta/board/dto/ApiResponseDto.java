package com.sparta.board.dto;

import com.sparta.board.exception.StatusCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDto {
    private String msg;
    private int httpStatus;

    public ApiResponseDto(StatusCode statusCode) {
        this.msg = statusCode.getMessage();
        this.httpStatus = statusCode.getHttpStatus().value();
    }


}
