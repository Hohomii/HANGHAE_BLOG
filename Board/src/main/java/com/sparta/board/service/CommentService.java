package com.sparta.board.service;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.entity.*;
import com.sparta.board.exception.CustomException;
import com.sparta.board.exception.StatusCode;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentLikeRepository;
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
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(
                () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
        );
        Comment comment = commentRepository.saveAndFlush(new Comment(requestDto, board, user));
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long cmtId, CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(
                () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
        );
        UserRoleEnum userRoleEnum = user.getRole();

        if (userRoleEnum == UserRoleEnum.USER) {
            Comment comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
                    () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
            );
            comment.updateComment(requestDto, board);
            return new CommentResponseDto(comment);
        } else {
            Comment comment = commentRepository.findById(cmtId).orElseThrow(
                    () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
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
                    () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
            );

            if (!comment.getUser().getId().equals(user.getId())) {
                throw new CustomException(StatusCode.INVALID_USER);
            }
            commentRepository.delete(comment);
        } else {
            Comment comment = commentRepository.findById(cmtId).orElseThrow(
                    () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
            );
            commentRepository.delete(comment);
        }
    }

    @Transactional
    public boolean likeComment(Long cmtId, User user) {
        Comment comment = commentRepository.findById(cmtId).orElseThrow(
                () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
        );

        if (commentLikeRepository.findByCommentAndUser(comment, user) == null) {
            comment.setLikeCount(comment.getLikeCount() + 1);
            CommentLike commentLike = new CommentLike(comment, user);
            commentLikeRepository.save(commentLike);
            return commentLike.isStatus();
        } else {
            CommentLike commentLike = commentLikeRepository.findByCommentAndUser(comment, user);
            commentLike.unLikeComment(comment);
            commentLikeRepository.delete(commentLike);
            return commentLike.isStatus();
        }
    }
}
