package me.choizz.chattingserver.websocket.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.choizz.chattingserver.websocket.ChatMessage;
import org.springframework.stereotype.Repository;

@Repository
public class ChatMessageMapRepository implements ChatMessageRepository {

    private static final Map<String, Map<String, String>> store = new ConcurrentHashMap<>();

    @Override
    public ChatMessage save(final ChatMessage chatMessage) {
        Map<String, String> userChat = new ConcurrentHashMap<>();
        userChat.put(chatMessage.getNickname(), chatMessage.getMessage());
        store.put(chatMessage.getRoomId(), userChat);

        return chatMessage;
    }
}
