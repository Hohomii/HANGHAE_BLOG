package com.sparta.board.service;
import com.sparta.board.dto.BoardResponseDto;
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
        User user = authenticateUser(request);
        Board board = boardRepository.saveAndFlush(new Board(requestDto, user));
        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, HttpServletRequest request) {
        User user = authenticateUser(request);
        Board board = boardRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new NullPointerException("해당 글이 없거나, 수정할 권한이 없습니다.")
        );
        board.updateBoard(requestDto);
        return new BoardResponseDto(board);
    }

    @Transactional
    public void deleteBoard(Long id, HttpServletRequest request) {
        User user = authenticateUser(request);
        Board board = boardRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 없거나, 삭제할 권한이 없습니다.")
        );
        boardRepository.delete(board);
    }

    //헤더에 있는 요청값으로 jwt 토큰 인증하는 메서드
    private User authenticateUser(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
        } else {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
    }
}
