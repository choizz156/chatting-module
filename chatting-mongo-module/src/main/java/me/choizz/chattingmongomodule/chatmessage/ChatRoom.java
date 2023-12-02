package me.choizz.chattingmongomodule.chatmessage;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@NoArgsConstructor
@Document("chat_room")
public class ChatRoom extends BaseMongoEntity {

    @MongoId(targetType = FieldType.INT64)
    private Long roomId;
    List<ChatMessage> messageList = new ArrayList<>();

    @Builder
    private ChatRoom(final Long roomId, final ChatMessage chatMessage) {
        this.roomId = roomId;
        this.messageList.add(chatMessage);
    }

    public static ChatRoom of(Long roomId){
        return ChatRoom.builder().roomId(roomId).build();
    }

    public static ChatRoom of(Long roomId, ChatMessage chatMessage){
        return ChatRoom.builder().roomId(roomId).chatMessage(chatMessage).build();
    }

}
