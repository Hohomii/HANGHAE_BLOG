package com.sparta.board.service;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.exception.CustomException;
import com.sparta.board.exception.ErrorCode;
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
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_LOGIN)
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
        //사용자 권한 가져와서 ADMIN이면 모든 게시글 수정 가능
        UserRoleEnum userRoleEnum = user.getRole();

        if (userRoleEnum == UserRoleEnum.USER) {
            Board board = boardRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(ErrorCode.INVALID_USER)
            );
            board.updateBoard(requestDto);
            return new BoardResponseDto(board);
        } else {
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new CustomException(ErrorCode.NULL_BOARD)
            );
            board.updateBoard(requestDto);
            return new BoardResponseDto(board);
        }
    }

    @Transactional
    public void deleteBoard(Long id, HttpServletRequest request) {
        User user = authenticateUser(request);
        UserRoleEnum userRoleEnum = user.getRole();

        if (userRoleEnum == UserRoleEnum.USER) {
            Board board = boardRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(ErrorCode.INVALID_USER)
            );
            boardRepository.delete(board);
        } else {
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new CustomException(ErrorCode.NULL_BOARD)
            );
            boardRepository.delete(board);
        }
    }

    //헤더에 있는 요청값으로 jwt 토큰 인증하는 메서드
    private User authenticateUser(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
            return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(ErrorCode.INVALID_LOGIN)
            );
        } else {
            throw new CustomException(ErrorCode.NULL_TOKEN);
        }
    }
}
