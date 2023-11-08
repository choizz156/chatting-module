package me.choizz.chattingserver.websocket.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import me.choizz.chattingserver.websocket.ChatMessage;
import me.choizz.chattingserver.websocket.ChatRoom;
import me.choizz.chattingserver.websocket.dto.ChatInfo;
import me.choizz.chattingserver.websocket.repository.ChatMessageRepository;
import me.choizz.chattingserver.websocket.repository.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatService chatService;

    @BeforeEach
    void setUpEach() {
        chatRoomRepository.delete();
        chatRoomRepository.delete();
    }

    @DisplayName("채팅룸을 생성한다.")
    @Test
    void chatRoom() throws Exception {
        //given
        ChatRoom chatRoom = chatService.createChatRoom("testRoom");

        //when
        chatRoomRepository.save(chatRoom);

        //then
        ChatRoom result = chatRoomRepository.findById(1L);
        assertThat(result.getName()).isEqualTo("testRoom");
        assertThat(result.getRoomId()).isNotEmpty();
    }

    @DisplayName("채팅 내역이 저장된다.")
    @Test
    void msg() throws Exception {
        //given
        List<ChatInfo> chatInfos = List.of(
            new ChatInfo("test", "roomId", "roomName", "testmsg1"),
            new ChatInfo("test", "roomId", "roomName", "testmsg2"),
            new ChatInfo("test", "roomId", "roomName", "testmsg3")
        );
        List<ChatMessage> chatMessageList = chatInfos.stream().map(ChatInfo::toEntity).toList();

        //when

        chatMessageList.forEach(c -> chatService.saveMassage(c));

        //then
        List<Map<String, String>> result = chatMessageRepository.findById("roomId");
        assertThat(result.get(0)).containsEntry("test", "testmsg1");
        assertThat(result.get(1)).containsEntry("test", "testmsg2");
        assertThat(result.get(2)).containsEntry("test", "testmsg3");
    }
}