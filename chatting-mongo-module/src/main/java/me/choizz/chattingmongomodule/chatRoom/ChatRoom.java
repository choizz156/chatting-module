package me.choizz.chattingmongomodule.chatRoom;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@NoArgsConstructor
@Document("chat_room")
public class ChatRoom{

    @MongoId(targetType = FieldType.INT64)
    private Long roomId;

    @DBRef
    private List<ChatMessage> messageList = new ArrayList<>();

    @Builder
    private ChatRoom(final Long roomId, final ChatMessage chatMessage) {
        this.roomId = roomId;
        this.messageList.add(chatMessage);
    }

    public static ChatRoom of(Long roomId) {
        return ChatRoom.builder().roomId(roomId).build();
    }

    public static ChatRoom of(Long roomId, ChatMessage chatMessage) {
        return ChatRoom.builder().roomId(roomId).chatMessage(chatMessage).build();
    }
}
