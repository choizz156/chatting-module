package me.choizz.chattingmongomodule.chatmessage;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChatMessage {

    private String id;
    private Long senderId;
    private Long receiverId;
    private String senderNickname;
    private String receiverNickname;
    private String content;
    private LocalDateTime createAt;

    @Builder
    public ChatMessage(
        final Long senderId,
        final Long receiverId,
        final String senderNickname,
        final String receiverNickname,
        final String content,
        final LocalDateTime createAt
    ) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderNickname = senderNickname;
        this.receiverNickname = receiverNickname;
        this.content = content;
        this.createAt = createAt;
    }
}
