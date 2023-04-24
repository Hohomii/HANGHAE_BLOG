package com.sparta.board.entity;

import com.sparta.board.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;

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
