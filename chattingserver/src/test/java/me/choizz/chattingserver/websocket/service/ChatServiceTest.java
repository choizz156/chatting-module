package me.choizz.chattingserver.websocket.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.util.List;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUpEach() {
        chatRoomRepository.deleteAll();
        chatRoomRepository.deleteAll();
    }

    @DisplayName("채팅룸을 생성한다.")
    @Test
    void chatRoom() throws Exception {
        //given
        chatService.createChatRoom("testRoom");

        //when
        ChatRoom result = chatRoomRepository.findAll().get(0);

        //then
        assertThat(result.getName()).isEqualTo("testRoom");
        assertThat(result.getRoomId()).isNotBlank();

    }

    @DisplayName("채팅 내역이 저장된다.")
    @Test
    void msg() throws Exception {
        //given
        ChatRoom chatRoom = chatService.createChatRoom("roomName");

        List<ChatInfo> chatInfos = List.of(
            new ChatInfo(chatRoom.getRoomId(), "test", "roomName", "testmsg1"),
            new ChatInfo(chatRoom.getRoomId(),"test", "roomName", "testmsg2"),
            new ChatInfo(chatRoom.getRoomId(),"test", "roomName", "testmsg3")
        );

        List<ChatMessage> chatMessageList = chatInfos.stream().map(ChatInfo::toEntity).toList();

        //when
        chatMessageList.forEach(c -> chatService.saveMassage(c));

        //then
        List<ChatMessage> result = chatMessageRepository.findAll();
        assertThat(result)
            .extracting("nickname", "message", "roomName")
            .containsAnyOf(
                tuple("test", "testmsg1", "roomName"),
                tuple("test", "testmsg2", "roomName"),
                tuple("test", "testmsg3", "roomName")
            );
    }
}