package me.choizz.chattingmongomodule.chatmessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final MongoTemplate mongoTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public void saveMassage(final String roomId, final ChatMessage chatMessage) {
        Update update = new Update().push("messageList", chatMessage);
//        mongoTemplate.findAndModify(
//            Query.query(Criteria.where("id").is(roomId)),
//            update,
//            ChattingRoom.class
//        )
    }
}
