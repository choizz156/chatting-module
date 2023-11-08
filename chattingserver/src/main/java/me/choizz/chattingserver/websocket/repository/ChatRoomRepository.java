package me.choizz.chattingserver.websocket.repository;

import me.choizz.chattingserver.websocket.ChatRoom;


public interface ChatRoomRepository {

    ChatRoom save(ChatRoom chatRoom);

}
