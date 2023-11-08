package me.choizz.chattingserver.websocket.repository;

import java.util.List;
import java.util.Map;
import me.choizz.chattingserver.websocket.ChatMessage;


public interface ChatMessageRepository {

    ChatMessage save(ChatMessage chatMessage);

    void delete();

    List<Map<String, String>> findById(String id);
}
