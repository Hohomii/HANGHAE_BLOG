package com.sparta.board.repository;

import com.sparta.board.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    //db에서 유저 정보로 refreshToken 찾기
    Optional<RefreshToken> findByUsername(String username);
}
