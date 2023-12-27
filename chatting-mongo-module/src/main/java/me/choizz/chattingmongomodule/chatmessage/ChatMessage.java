package me.choizz.chattingmongomodule.chatmessage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@NoArgsConstructor
@Getter
@Document("chat_message")
public class ChatMessage extends BaseMongoEntity {

    @Id
    private String id;
    private Long roomId;
    private Long senderId;
    private Long receiverId;
    private String senderNickname;
    private String receiverNickname;
    private String content;

    @Builder
    public ChatMessage(
        final Long roomId,
        final Long senderId,
        final Long receiverId,
        final String senderNickname,
        final String receiverNickname,
        final String content
    ) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderNickname = senderNickname;
        this.receiverNickname = receiverNickname;
        this.content = content;
    }
}
