package com.sparta.board.service;

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
                .collect(Collectors.toList());
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new BoardResponseDto(board);
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

            Board board = boardRepository.saveAndFlush(new Board(requestDto, user));
            return new BoardResponseDto(board);
        } else {
            return null;
        }
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, HttpServletRequest request) {
        // 헤더에서 토큰을 받아와 token에 저장
        // JWT를 통해 전송되는 암호화된 정보를 담기 위한 객체 claims 생성
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 null값이 아닐 때 토큰이 유효하면 claims에 암호화된 사용자정보를 넣음
        // 유효하지 않으면 토큰 에러 예외 던지기
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // userropository에서 username을 찾는데, claims에서 받아온 정보를 넣어서 찾음
            // 찾아서 user에 넣음 (여기서 왜 entity인 user에 넣는 걸까? 그냥 변수가 아니라.)
            // ===> 밑에서 userId 외래키를 사용해 user의 해당 id에 접근하기 위해 쓰이게 됨. 신기하군...
            // 정보 없으면 예외 던지기
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );


            // boardRepository에서 id,userId를 찾는데, 그중 userId(외래키)는 위에서 저장한 user 엔티티값의 id임
            Board board = boardRepository.findByIdAndUserId(id, user).orElseThrow(
                    () -> new NullPointerException("해당 글은 존재하지 않습니다.")
            );
            board.updateBoard(requestDto);
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


            Board board = boardRepository.findByIdAndUserId(id, user).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            boardRepository.delete(board);
        }
    }
}
