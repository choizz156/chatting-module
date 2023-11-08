package me.choizz.chattingserver.websocket.repository;

import me.choizz.chattingserver.websocket.ChatMessage;


public interface ChatMessageRepository {

    ChatMessage save(ChatMessage chatMessage);

}
