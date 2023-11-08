package me.choizz.chattingserver.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.websocket.ChatMessage;
import me.choizz.chattingserver.websocket.ChatRoom;
import me.choizz.chattingserver.websocket.repository.ChatMessageRepository;
import me.choizz.chattingserver.websocket.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoom createChatRoom(String name) {
        ChatRoom room = new ChatRoom(name);
        chatRoomRepository.save(room);
        return room;
    }

    public void saveMassage(final ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }
}
