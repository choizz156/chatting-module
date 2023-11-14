package me.choizz.chattingserver.websocket;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
public class ChatRoom {

    @Id
    private String roomId;
//    private String roomId = createId();
    private String name;

    public ChatRoom(final String name) {
        this.name = name;
    }

//    private String createId() {
//        return UUID.randomUUID().toString().substring(0, 8);
//    }
}
