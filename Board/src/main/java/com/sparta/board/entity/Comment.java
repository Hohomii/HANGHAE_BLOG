package com.sparta.board.entity;

import com.sparta.board.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column
    private int likeCount;

    public Comment(CommentRequestDto requestDto, Board board, User user) {
        this.content = requestDto.getContent();
        this.board = board;
        this.user = user;
        this.likeCount = 0;
    }

    public void updateComment(CommentRequestDto requestDto, Board board) {
        this.content = requestDto.getContent();
        this.board = board;
    }



}
