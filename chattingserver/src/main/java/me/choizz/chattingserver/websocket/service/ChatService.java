package me.choizz.chattingserver.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.websocket.domain.ChatMessage;
import me.choizz.chattingserver.websocket.domain.ChattingRoom;
import me.choizz.chattingserver.websocket.repository.ChatMessageRepository;
import me.choizz.chattingserver.websocket.repository.ChatRoomRepository;
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
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChattingRoom createChattingRoom(String name) {
        var room = new ChattingRoom(name);
        return chatRoomRepository.save(room);
    }

    public void saveMassage(final String roomId, final ChatMessage chatMessage) {
        var update = new Update().push("messageList", chatMessage);
        mongoTemplate.findAndModify(
            Query.query(Criteria.where("id").is(roomId)),
            update,
            ChattingRoom.class
        );
    }
}
