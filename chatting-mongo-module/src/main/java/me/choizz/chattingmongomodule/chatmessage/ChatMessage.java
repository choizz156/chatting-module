package me.choizz.chattingmongomodule.chatmessage;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.choizz.chattingmongomodule.dto.ChatMessageDto;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document("chat_room")
public class ChatMessage extends BaseMongoEntity{


    @Indexed(unique = true)
    private Long roomId;

    List<ChatMessageDto> messageList = new ArrayList<>();

    public ChatMessage(final Long roomId, final ChatMessageDto chatMessageDto) {
        this.roomId = roomId;
        this.messageList.add(chatMessageDto);
    }
}
