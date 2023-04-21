package com.sparta.board.service;


import com.sparta.board.dto.MsgResponseDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.entity.User;
import com.sparta.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;

    @Transactional
    public MsgResponseDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // Optional : Null이 올 수 있는 값을 감싸는 Wrapper 클래스. NullPointerException을 방지해줌
        // isPresent : Optional이 제공하는 메서드. Boolean타입. Optional 객체가 값을 가지고 있다면 true, 없으면 false 리턴
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(username, password);
        userRepository.save(user);
        return new MsgResponseDto();
    }

}
