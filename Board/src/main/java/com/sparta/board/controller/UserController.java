package com.sparta.board.controller;

import com.sparta.board.dto.LoginRequestDto;
import com.sparta.board.dto.ApiResponseDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.exception.ErrorResponse;
import com.sparta.board.exception.StatusCode;
import com.sparta.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.sparta.board.exception.StatusCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // ResponseEntity : 결과 데이터와 HTTP 상태 코드를 직접 제어할 수 있는 클래스(같이 전달해줌)
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
