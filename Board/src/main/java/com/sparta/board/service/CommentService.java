package com.sparta.board.service;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentRepository;
import com.sparta.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final JwtUtil jwtUtil;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        User user = authenticateUser(request);
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 없습니다.")
        );
        Comment comment = commentRepository.saveAndFlush(new Comment(requestDto, board, user));
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, Long cmtId, CommentRequestDto requestDto, HttpServletRequest request) {
        User user = authenticateUser(request);
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 없습니다.")
        );
        UserRoleEnum userRoleEnum = user.getRole();

        if (userRoleEnum == UserRoleEnum.USER) {
            Comment comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 댓글이 없거나, 수정할 권한이 없습니다.")
            );
            comment.updateComment(requestDto, board);
            return new CommentResponseDto(comment);
        } else {
            Comment comment = commentRepository.findById(cmtId).orElseThrow(
                    () -> new NullPointerException("해당 댓글이 없습니다.")
            );
            comment.updateComment(requestDto, board);
            return new CommentResponseDto(comment);
        }
    }




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
