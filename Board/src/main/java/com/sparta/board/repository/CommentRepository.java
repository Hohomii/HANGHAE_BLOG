package com.sparta.board.repository;

import com.sparta.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByOrderByModifiedAtDesc();

    Optional<Comment> findByIdAndCreatedBy(Long cmtId, String createdBy);
}
