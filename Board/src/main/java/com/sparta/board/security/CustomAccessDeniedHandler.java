package com.sparta.board.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board.exception.StatusCode;
import com.sparta.board.exception.ErrorResponse;
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

        response.setContentType("application/json; charset=utf8");

        String json = new ObjectMapper().writeValueAsString(ErrorResponse.responseEntity(StatusCode.USER_NOT_FOUND).getBody());
        response.getWriter().write(json);
    }
}
