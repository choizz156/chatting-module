package me.choizz.chattingmongomodule.dto;

import java.time.LocalDateTime;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;

public record ChatMessageDto(
    Long roomId,
    Long senderId,
    Long receiverId,
    String senderNickname,
    String receiverNickname,
    String content
) {
    public ChatMessage toEntity() {
        return ChatMessage.builder()
            .senderId(senderId())
            .receiverId(receiverId())
            .senderNickname(senderNickname())
            .receiverNickname(receiverNickname())
            .content(content())
            .createAt(LocalDateTime.now())
            .build();
    }
}
