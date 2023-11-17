package me.choizz.chattingserver.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.domain.chattingroom.ChattingRoom;
import me.choizz.chattingserver.websocket.domain.ChatMessage;
import me.choizz.chattingserver.websocket.repository.ChatMessageRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final MongoTemplate mongoTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public void saveMassage(final String roomId, final ChatMessage chatMessage) {
        var update = new Update().push("messageList", chatMessage);
        mongoTemplate.findAndModify(
            Query.query(Criteria.where("id").is(roomId)),
            update,
            ChattingRoom.class
        );
    }
}
