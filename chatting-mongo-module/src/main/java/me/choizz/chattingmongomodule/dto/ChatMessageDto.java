package me.choizz.chattingmongomodule.dto;

import java.time.LocalDateTime;
import lombok.Builder;

public record ChatMessageDto(
    String nickname,
    String message,
    LocalDateTime createdAt
) {
    @Builder
    public ChatMessageDto(
        final String nickname,
        final String message,
        final LocalDateTime createdAt
    ) {
        this.nickname = nickname;
        this.message = message;
        this.createdAt = createdAt;
    }

}
