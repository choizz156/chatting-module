package me.choizz.chattingserver.websocket.dto;

import lombok.Builder;
import me.choizz.chattingserver.websocket.ChatMessage;

@Builder
public record ChatInfo(String nickname, String roomId, String roomName, String msg) {

    public ChatMessage toEntity() {
        return ChatMessage.builder()
            .message(msg())
            .roomId(roomId())
            .nickname(nickname())
            .build();
    }

}
