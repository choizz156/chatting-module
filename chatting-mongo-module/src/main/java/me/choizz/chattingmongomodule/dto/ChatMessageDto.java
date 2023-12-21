package me.choizz.chattingmongomodule.dto;

import me.choizz.chattingmongomodule.chatmessage.ChatMessage;

public record ChatMessageDto(
    String id,
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
            .build();
    }
}
