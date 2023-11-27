package me.choizz.chattingmongomodule.chatmessage;

import me.choizz.chattingmongomodule.dto.ChatMessageDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessageDto, String> {

    boolean existsBy(Long roomId);
}
