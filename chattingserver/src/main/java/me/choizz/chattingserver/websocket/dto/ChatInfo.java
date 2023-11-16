package me.choizz.chattingserver.websocket.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Builder;
import me.choizz.chattingserver.websocket.domain.ChatMessage;

@Builder
public record ChatInfo(@NotEmpty String nickname, @NotEmpty String msg) {

    public ChatMessage toEntity(LocalDateTime createTime) {
        return ChatMessage.builder()
            .message(msg())
            .nickname(nickname())
            .createdAt(createTime)
            .build();
    }

}
