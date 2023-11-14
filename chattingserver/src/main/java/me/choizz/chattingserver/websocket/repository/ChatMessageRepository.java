package me.choizz.chattingserver.websocket.repository;

import me.choizz.chattingserver.websocket.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {

}
