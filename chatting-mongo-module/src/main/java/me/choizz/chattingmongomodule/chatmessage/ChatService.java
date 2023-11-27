package me.choizz.chattingmongomodule.chatmessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.dto.ChatMessageDto;
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

    public void saveMassage(final Long roomId, final ChatMessageDto chatMessageDto) {
        log.error("{}", roomId);
        checkExistChatRoom(roomId, chatMessageDto);

        Update update = new Update().push("messageList", chatMessageDto);
        mongoTemplate.findAndModify(
            Query.query(Criteria.where("roomId").is(roomId)),
            update,
            ChatMessage.class
        );
    }

    private void checkExistChatRoom(final Long roomId, final ChatMessageDto chatMessageDto) {
        if (!chatMessageRepository.existsBy(roomId)) {
            log.error("없음");
            ChatMessage chatMessage = new ChatMessage(roomId, chatMessageDto);
            chatMessageRepository.save(chatMessageDto);
            log.error("save");
            return;
        }
        log.error("있음");
    }
}
