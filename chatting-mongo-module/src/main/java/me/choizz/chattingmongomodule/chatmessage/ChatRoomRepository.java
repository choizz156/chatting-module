package me.choizz.chattingmongomodule.chatmessage;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, Long> {

}
