package com.sparta.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String adminToken;
    private boolean isAdmin;

    public boolean isAdmin() {
        if(adminToken != null) {
            return true;
        }
        return false;
    }
}
