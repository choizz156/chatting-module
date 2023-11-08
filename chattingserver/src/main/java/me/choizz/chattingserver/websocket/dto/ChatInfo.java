package me.choizz.chattingserver.websocket.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import me.choizz.chattingserver.websocket.ChatMessage;

@Builder
public record ChatInfo(@NotEmpty String nickname, @NotEmpty String roomId, String roomName, @NotEmpty String msg) {

    public ChatMessage toEntity() {
        return ChatMessage.builder()
            .message(msg())
            .roomId(roomId())
            .roomName(roomName().isEmpty() ? nickname() :roomName())
            .nickname(nickname())
            .build();
    }

}
