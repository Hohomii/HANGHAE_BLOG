package com.sparta.board.controller;

import com.sparta.board.dto.ApiResponseDto;
import com.sparta.board.dto.LoginRequestDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.sparta.board.exception.StatusCode.LOGIN_OK;
import static com.sparta.board.exception.StatusCode.SIGNUP_OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponseDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return new ApiResponseDto(SIGNUP_OK);
    }

    @ResponseBody
    @PostMapping("/login")
    public ApiResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return new ApiResponseDto(LOGIN_OK);
    }

}
