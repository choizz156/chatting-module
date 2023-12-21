package me.choizz.chattingmongomodule.chatmessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatRoom.ChatRoom;
import me.choizz.chattingmongomodule.chatRoom.ChatRoomRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final MongoTemplate mongoTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public void saveMassage(final Long roomId, final ChatMessage chatMessage) {
        checkExistChatRoom(roomId, chatMessage);
        ChatMessage entity = chatMessageRepository.save(chatMessage);

        insertMessage(roomId, entity);
    }

    public Optional<List<ChatMessage>> findChatMessages(Long roomId) {
        return getChatMessages(roomId);
    }

    public void deleteMessage(final String id) {
        chatMessageRepository.deleteById(id);
    }

    private Optional<List<ChatMessage>> getChatMessages(final Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            ChatRoom chatRoom = ChatRoom.of(roomId);
            chatRoomRepository.save(chatRoom);
            return Optional.of(new ArrayList<>());
        }
        Optional<ChatRoom> byId = chatRoomRepository.findById(roomId);
        return chatRoomRepository.findById(roomId).map(ChatRoom::getMessageList);
    }

    private void insertMessage(final Long roomId, final ChatMessage entity) {
        Update update = new Update().push("messageList", entity);
        mongoTemplate.findAndModify(
            Query.query(Criteria.where("roomId").is(roomId)),
            update,
            ChatRoom.class
        );
    }


    private void checkExistChatRoom(final Long roomId, final ChatMessage chatMessage) {
        if (!chatRoomRepository.existsById(roomId)) {
            chatMessageRepository.save(chatMessage);
            ChatRoom chatRoom = ChatRoom.of(roomId, chatMessage);
            chatRoomRepository.save(chatRoom);
            return;
        }
    }
}
