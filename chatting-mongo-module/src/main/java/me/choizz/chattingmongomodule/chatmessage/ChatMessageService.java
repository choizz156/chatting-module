package me.choizz.chattingmongomodule.chatmessage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatRoom.ChatRoom;
import me.choizz.chattingmongomodule.chatRoom.ChatRoomRepository;
import me.choizz.chattingmongomodule.exception.WebSocketBusinessException;
import me.choizz.chattingmongomodule.exception.WebSocketExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");

    private final MongoTemplate mongoTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;


    public ChatMessage saveMassage(final Long roomId, final ChatMessage chatMessage) {
        ChatMessage message = chatMessageRepository.save(chatMessage);
        logger.info("save message in messageRepository");

        insertMessage(roomId, message);
        return message;
    }

    public List<ChatMessage> findChatMessages(Long roomId) {
        return getChatMessages(roomId);
    }

    public void deleteMessage(final String id) {
        chatMessageRepository.deleteById(id);
        logger.info("sent message delete => messageID : {}", id);
    }

    private List<ChatMessage> getChatMessages(final Long roomId) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(roomId);
        if (optionalChatRoom.isPresent()) {
            return Collections.unmodifiableList(optionalChatRoom.get().getMessageList());
        }
        throw new WebSocketBusinessException(WebSocketExceptionCode.NO_CHAT_ROOM);
    }

    private void insertMessage(final Long roomId, final ChatMessage entity) {
        Update update = new Update().push("messageList", entity);
        mongoTemplate.findAndModify(
            Query.query(Criteria.where("roomId").is(roomId)),
            update,
            ChatRoom.class
        );
        logger.info("insert message in chatRoom {}", roomId);
    }

    public void checkExistChatRoom(final Long roomId) {
        if (isNotExistRoom(roomId)) {
            logger.info("checkExistChatRoom => chatRoomId: {}", roomId);
            ChatRoom chatRoom = ChatRoom.of(roomId);
            chatRoomRepository.save(chatRoom);
            logger.info("create new Room => new chatRoomId: {}", roomId);
        }
    }

    private boolean isNotExistRoom(final Long roomId) {
        return !chatRoomRepository.existsById(roomId);
    }
}
