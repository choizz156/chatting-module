package me.choizz.chattingserver.websocket;

import java.util.UUID;
import lombok.Getter;

@Getter
public class ChatRoom {

    private String roomId = createId();
    private String name;

    public ChatRoom(final String name) {
        this.name = name;
    }

    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
