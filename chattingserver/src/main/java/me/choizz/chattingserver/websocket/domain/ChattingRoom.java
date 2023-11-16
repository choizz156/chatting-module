package me.choizz.chattingserver.websocket.domain;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat_room")
@Getter
@NoArgsConstructor
public class ChattingRoom extends BaseEntity{

    @Id
    private String id;
    private String name;

    private List<ChatMessage> messages;

    public ChattingRoom(final String name) {
        this.name = name;
    }

//    private String createId() {
//        return UUID.randomUUID().toString().substring(0, 8);
//    }
}
