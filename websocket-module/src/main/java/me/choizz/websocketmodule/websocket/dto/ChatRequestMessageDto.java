package me.choizz.websocketmodule.websocket.dto;

import lombok.Builder;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;

@Builder
public record ChatRequestMessageDto(
    Long roomId,
    Long senderId,
    Long receiverId,
    String senderNickname,
    String receiverNickname,
    String content
) {
    public ChatMessage toEntity() {
        return ChatMessage.builder()
            .roomId(roomId())
            .senderId(senderId())
            .receiverId(receiverId())
            .senderNickname(senderNickname())
            .receiverNickname(receiverNickname())
            .content(content())
            .build();
    }
}
