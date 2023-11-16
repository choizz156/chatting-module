package me.choizz.chattingserver.websocket.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessage {

    private String nickname;
    private String message;
    private LocalDateTime createdAt;

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
