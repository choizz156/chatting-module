package me.choizz.chattingmongomodule.chatmessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void saveMassage(final String roomId, final ChatMessage chatMessage) {
        checkExistChatRoom(roomId, chatMessage);

        Update update = new Update().push("messageList", chatMessage);
        mongoTemplate.findAndModify(
            Query.query(Criteria.where("roomId").is(roomId)),
            update,
            ChatRoom.class
        );
    }

    public Optional<List<ChatMessage>> findChatMessages(String roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            ChatRoom chatRoom = ChatRoom.of(roomId);
            chatRoomRepository.save(chatRoom);
            return Optional.of(new ArrayList<>());
        }
        return chatRoomRepository.findById(roomId).map(ChatRoom::getMessageList);
    }


    private void checkExistChatRoom(final String roomId, final ChatMessage chatMessage) {
        if (!chatRoomRepository.existsById(roomId)) {
            ChatRoom chatRoom = ChatRoom.of(roomId, chatMessage);
            chatRoomRepository.save(chatRoom);
            return;
        }
    }
}
