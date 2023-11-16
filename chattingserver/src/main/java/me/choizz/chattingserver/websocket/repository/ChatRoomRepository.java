package me.choizz.chattingserver.websocket.repository;

import me.choizz.chattingserver.websocket.domain.ChattingRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChattingRoom, String> {
}
