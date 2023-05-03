package com.sparta.board.repository;

import com.sparta.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByOrderByModifiedAtDesc(); // 작성일 기준 내림차순 정렬 리스트

    Optional<Board> findByIdAndCreatedBy(Long id, String createdBy);
}