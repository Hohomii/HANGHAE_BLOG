package com.sparta.board.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board.exception.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException{

        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(ErrorCode.INVALID_LOGIN.getHttpStatus().value());

        String json = new ObjectMapper().writeValueAsString(ErrorCode.INVALID_LOGIN);
        response.getWriter().write(json);

    }
}
