package me.choizz.chattingmongomodule.dto;

import me.choizz.chattingmongomodule.user.LoginUser;

public record LoginUserDto(Long userId, String email, String nickname) {

    public LoginUser toEntity() {
        return LoginUser.builder()
            .userId(userId())
            .nickname(nickname())
            .email(email())
            .build();
    }
}
