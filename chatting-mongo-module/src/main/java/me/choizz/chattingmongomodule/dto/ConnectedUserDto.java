package me.choizz.chattingmongomodule.dto;

import me.choizz.chattingmongomodule.user.ConnectedUser;

public record ConnectedUserDto(Long userId, String email, String nickname) {

    public ConnectedUser toEntity() {
        return ConnectedUser.builder()
            .userId(userId())
            .nickname(nickname())
            .email(email())
            .build();
    }
}
