package me.choizz.chattingmongomodule.chatmessage;

import java.time.LocalDateTime;
import lombok.Builder;

public record ChatMessage(String nickname, String message, LocalDateTime createdAt) {

    @Builder
    public ChatMessage(
        final String nickname,
        final String message,
        final LocalDateTime createdAt
    ) {
        this.nickname = nickname;
        this.message = message;
        this.createdAt = createdAt;
    }

}
