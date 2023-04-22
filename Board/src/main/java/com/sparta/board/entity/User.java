package com.sparta.board.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Entity(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "이름은 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 이루어져야 합니다.")
    private String username;

    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$", message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 이루어져야 합니다.")
    private String password;

    @OneToMany(mappedBy = "user")
    @Column
    private List<Board> boards;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}