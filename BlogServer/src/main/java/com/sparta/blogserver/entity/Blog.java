package com.sparta.blogserver.entity;

import com.sparta.blogserver.dto.PostingRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class Blog extends Timestamped {
    @Id // PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 id 고유번호 생성됨(1씩 추가)
    private Long id;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String password;


    public Blog(PostingRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.writer = requestDto.getWriter();
        this.content = requestDto.getContent();
        this.password = requestDto.getPassword();
    }

    public void updatePosting(PostingRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.writer = requestDto.getWriter();
        this.password = requestDto.getPassword();
    }
}
