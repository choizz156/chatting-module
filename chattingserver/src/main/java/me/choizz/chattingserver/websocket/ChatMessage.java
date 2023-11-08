package me.choizz.chattingserver.websocket;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessage {

    private String roomId;
    private String nickname;
    private String message;

    @Builder
    public ChatMessage(final String roomId, final String nickname, final String message) {
        this.roomId = roomId;
        this.nickname = nickname;
        this.message = message;
    }

}
