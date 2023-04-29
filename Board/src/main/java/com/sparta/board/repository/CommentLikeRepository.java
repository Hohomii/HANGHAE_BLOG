package com.sparta.board.repository;

import com.sparta.board.entity.Comment;
import com.sparta.board.entity.CommentLike;
import com.sparta.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    CommentLike findByCommentAndUser(Comment comment, User user);
}
