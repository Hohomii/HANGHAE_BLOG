package com.sparta.board.repository;

import com.sparta.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByOrderByModifiedAtDesc(Pageable pageable);

    Optional<Board> findByIdAndCreatedBy(Long id, String createdBy);
}