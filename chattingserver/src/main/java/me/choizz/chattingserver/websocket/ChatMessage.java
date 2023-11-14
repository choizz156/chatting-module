package me.choizz.chattingserver.websocket;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
public class ChatMessage {

    @Id
    private String id;
    private String roomId;
    private String roomName;
    private String nickname;
    private String message;

    @Builder
    public ChatMessage(
        final String nickname,
        final String message,
        final String roomName,
        final String roomId
    ) {
        this.nickname = nickname;
        this.message = message;
        this.roomName = roomName;
        this.roomId = roomId;
    }

}
