package com.sparta.board.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board.exception.ErrorCode;
import com.sparta.board.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@RequiredArgsConstructor
//인증 예외
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException {

        response.setContentType("application/json; charset=utf8");

        String json = new ObjectMapper().writeValueAsString(ErrorResponse.responseEntity(ErrorCode.INVALID_TOKEN));
        response.getWriter().write(json);
    }
}
