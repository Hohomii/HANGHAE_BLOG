package com.sparta.board.Service;

import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.MsgResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.User;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 생성자 주입 위한 어노테이션
public class BoardService {

    private final BoardRepository boardRepository; // 생성자 주입
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 글 전체 조회. Jpa 쿼리 메서드 사용해서 작성일 기준 내림차순. 반환값 리스트
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards() {
        return boardRepository.findByOrderByModifiedAtDesc().stream()
                .map(b -> new BoardResponseDto(b))
                .collect(Collectors.toList()); // 이 부분 공부 필요
    }

    // 글 작성 -> 로그인한 사용자만 작성 가능
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Board board = boardRepository.saveAndFlush(new Board(requestDto, user.getUsername()));
            return new BoardResponseDto(board);
        } else {
            return null;
        }
    }


    // 글 삭제. 로직은 글 수정하는 부분과 동일
    @Transactional
    public void deleteBoard(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );


            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            boardRepository.delete(board);
//            boardRepository.deleteById(id);
        }
    }
}
