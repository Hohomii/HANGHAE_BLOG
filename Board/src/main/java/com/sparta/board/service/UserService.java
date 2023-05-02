package com.sparta.board.service;

import com.sparta.board.dto.LoginRequestDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.dto.TokenDto;
import com.sparta.board.entity.RefreshToken;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.exception.CustomException;
import com.sparta.board.exception.StatusCode;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.RefreshTokenRepository;
import com.sparta.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(@RequestBody SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        //username 유효성 검사
        if (!isValidUsername(username)) {
            throw new CustomException(StatusCode.INVALID_SIGNUP_USERNAME);
        }
        //password 유효성 검사
        if(!isValidPassword(password)) {
            throw new CustomException(StatusCode.INVALID_SIGNUP_PASSWORD);
        }
        //password 암호화
        password = passwordEncoder.encode(password);

        //username 중복 검사
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new CustomException(StatusCode.DUPLICATE_USER);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomException(StatusCode.INVALID_ADMIN);
            }
            role = UserRoleEnum.ADMIN;
        }
        User user = new User(username, password, role);
        //회원가입 성공
        userRepository.save(user);
    }

    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        //username 검사
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(StatusCode.USER_NOT_FOUND)
        );
        //password 검사
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(StatusCode.INVALID_PASSWORD);
        }

        //username으로 Token 생성
        TokenDto tokenDto = jwtUtil.createAllToken(user.getUsername(), user.getRole());

        //RefreshToken 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(user.getUsername());

        //있으면 새 토큰 발급 후 업데이트
        //없으면 새로 만들고 DB에 저장
        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), username);
            refreshTokenRepository.save(newToken);
        }

        //response 헤더에 Access Token / Refresh Token 넣음
        setHeader(response, tokenDto);
    }

    private boolean isValidUsername(String username) {
        String usernamePattern = "^[a-z0-9]{4,10}$";
        return username.matches(usernamePattern);
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,15}$";
        return password.matches(passwordPattern);
    }

    public void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

}
