package me.choizz.chattingmongomodule.chatRoom;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, Long> {

}
