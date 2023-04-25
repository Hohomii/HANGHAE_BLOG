package com.sparta.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.board.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class Board extends Timestamped {
    @Id // PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 id 고유번호 생성됨(1씩 추가)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩 : DB조회 지연함으로써 성능 최적화
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    //댓글은 글 가져올 때 항상 다같이 가져오기.(즉시로딩)
    //글 삭제될 때 해당 글의 댓글도 같이 삭제하기.(cascade)
    //순환참조 방지(jsonignore)
    @JsonIgnore
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @Column
    private List<Comment> comments = new ArrayList<>();


    public Board(BoardRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = user.getUsername();
        this.user = user;

    }

    public void updateBoard(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();

    }
}
