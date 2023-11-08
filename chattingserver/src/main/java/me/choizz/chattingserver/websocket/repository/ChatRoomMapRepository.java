package me.choizz.chattingserver.websocket.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import me.choizz.chattingserver.websocket.ChatRoom;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomMapRepository implements ChatRoomRepository {

    private static final Map<Long, ChatRoom> store = new ConcurrentHashMap<>();
    private AtomicLong sequence = new AtomicLong();

    @Override
    public ChatRoom save(final ChatRoom chatRoom) {
        return store.put(sequence.getAndIncrement(), chatRoom);
    }

    @Override
    public void delete() {
        store.clear();
    }

    @Override
    public ChatRoom findById(final Long id) {
        return store.get(id);
    }
}
