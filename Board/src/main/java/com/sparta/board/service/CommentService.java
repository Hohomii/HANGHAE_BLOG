package com.sparta.board.service;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.exception.CustomException;
import com.sparta.board.exception.ErrorCode;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentRepository;
import com.sparta.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final JwtUtil jwtUtil;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(
                () -> new CustomException(ErrorCode.NULL_BOARD)
        );
        Comment comment = commentRepository.saveAndFlush(new Comment(requestDto, board, user));
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long cmtId, CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(
                () -> new CustomException(ErrorCode.NULL_BOARD)
        );
        UserRoleEnum userRoleEnum = user.getRole();

        if (userRoleEnum == UserRoleEnum.USER) {
            Comment comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
                    () -> new CustomException(ErrorCode.NULL_BOARD)
            );
            comment.updateComment(requestDto, board);
            return new CommentResponseDto(comment);
        } else {
            Comment comment = commentRepository.findById(cmtId).orElseThrow(
                    () -> new CustomException(ErrorCode.NULL_BOARD)
            );
            comment.updateComment(requestDto, board);
            return new CommentResponseDto(comment);
        }
    }

    @Transactional
    public void deleteComment(Long cmtId, User user) {
        UserRoleEnum userRoleEnum = user.getRole();

        if (userRoleEnum == UserRoleEnum.USER) {
            Comment comment = commentRepository.findById(cmtId).orElseThrow(
                    () -> new CustomException(ErrorCode.NULL_BOARD)
            );

            if (!comment.getUser().getId().equals(user.getId())) {
                throw new CustomException(ErrorCode.INVALID_USER);
            }
            commentRepository.delete(comment);
        } else {
            Comment comment = commentRepository.findById(cmtId).orElseThrow(
                    () -> new CustomException(ErrorCode.NULL_BOARD)
            );
            commentRepository.delete(comment);
        }
    }
}
