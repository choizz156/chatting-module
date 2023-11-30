package me.choizz.chattingmongomodule.chatmessage;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document("chat_room")
public class ChatRoom extends BaseMongoEntity {

    @Id
    private String roomId;
    List<ChatMessage> messageList = new ArrayList<>();

    @Builder
    private ChatRoom(final String roomId, final ChatMessage chatMessage) {
        this.roomId = roomId;
        this.messageList.add(chatMessage);
    }

    public static ChatRoom of(String roomId){
        return ChatRoom.builder().roomId(roomId).build();
    }

    public static ChatRoom of(String roomId, ChatMessage chatMessage){
        return ChatRoom.builder().roomId(roomId).chatMessage(chatMessage).build();
    }

}
