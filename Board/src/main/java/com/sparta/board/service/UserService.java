package com.sparta.board.service;
import com.sparta.board.dto.LoginRequestDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.exception.CustomException;
import com.sparta.board.exception.StatusCode;
import com.sparta.board.jwt.JwtUtil;
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

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(@RequestBody SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        if (!isValidUsername(username)) {
            throw new CustomException(StatusCode.INVALID_SIGNUP_USERNAME);
        }

        if(!isValidPassword(password)) {
            throw new CustomException(StatusCode.INVALID_SIGNUP_PASSWORD);
        }

        password = passwordEncoder.encode(password);

        // Optional : Null이 올 수 있는 값을 감싸는 Wrapper 클래스. NullPointerException을 방지해줌
        // isPresent : Optional이 제공하는 메서드. Boolean타입. Optional 객체가 값을 가지고 있다면 true, 없으면 false 리턴
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
        userRepository.save(user);
    }

    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(StatusCode.USER_NOT_FOUND)
        );

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(StatusCode.INVALID_PASSWORD);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
    }

    private boolean isValidUsername(String username) {
        String usernamePattern = "^[a-z0-9]{4,10}$";
        return username.matches(usernamePattern);
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,15}$";
        return password.matches(passwordPattern);
    }

}
