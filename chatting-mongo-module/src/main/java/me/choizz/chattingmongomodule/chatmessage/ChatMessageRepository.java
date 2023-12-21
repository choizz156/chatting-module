package me.choizz.chattingmongomodule.chatmessage;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {

}
