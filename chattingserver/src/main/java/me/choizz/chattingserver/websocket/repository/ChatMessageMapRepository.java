package me.choizz.chattingserver.websocket.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.websocket.ChatMessage;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ChatMessageMapRepository implements ChatMessageRepository {

    private static final Map<String, List<Map<String, String>>> store = new ConcurrentHashMap<>();

    @Override
    public ChatMessage save(final ChatMessage chatMessage) {
        Map<String, String> userChat = new ConcurrentHashMap<>();
        userChat.put(chatMessage.getNickname(), chatMessage.getMessage());

        List<Map<String, String>> lists =
            store.getOrDefault(chatMessage.getRoomId(), new ArrayList<>());
        lists.add(userChat);

        store.put(chatMessage.getRoomId(), lists);

        return chatMessage;
    }

    @Override
    public void delete() {
        store.clear();
    }

    @Override
    public List<Map<String, String>> findById(final String id) {
        return store.get(id);
    }
}
