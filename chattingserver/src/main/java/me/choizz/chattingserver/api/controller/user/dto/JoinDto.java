package me.choizz.chattingserver.api.controller.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import me.choizz.chattingserver.domain.user.User;

public record JoinDto(
    @Email
    String email,

    @NotEmpty
    String password,

    @NotEmpty
    String nickname
) {

    public User toEntity() {
        return User.builder()
            .email(email())
            .password(password())
            .nickname(nickname)
            .build();
    }
}
