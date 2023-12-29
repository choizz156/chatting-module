package me.choizz.chattingmongomodule.chatmessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.tuple;

import java.util.List;
import java.util.Optional;
import me.choizz.chattingmongomodule.chatRoom.ChatRoom;
import me.choizz.chattingmongomodule.chatRoom.ChatRoomRepository;
import me.choizz.chattingmongomodule.exception.WebSocketBusinessException;
import me.choizz.chattingmongomodule.exception.WebSocketExceptionCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = "me.choizz.chattingmongomodule")
@ContextConfiguration(classes =
    {
        ChatMessageRepository.class,
        ChatMessageService.class,
        ChatRoomRepository.class
    }
)
@DataMongoTest
class ChatMessageServiceTest {

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    ChatMessageService chatMessageService;

    @Autowired
    ChatRoomRepository chatRoomRepository;


    @BeforeEach
    void setUpEach() {
        chatMessageService.checkExistChatRoom(1L);
    }

    @AfterEach
    void tearDown() {
        chatRoomRepository.deleteAll();
        chatMessageRepository.deleteAll();
    }

    @DisplayName("chatRoom 도큐먼트가 존재하지 않는다면 생성한다.")
    @Test
    void test1() throws Exception {
        //give//when
        ChatRoom result = chatRoomRepository.findById(1L).orElseGet(ChatRoom::new);

        //then
        assertThat(result.getRoomId()).isEqualTo(1L);
    }

    @DisplayName("chatroom 도큐먼트가 존재한다면 메시지만 저장한다.")
    @Test
    void test2() throws Exception {
        //given
        ChatMessage chatMessage1 = getChatMessage1();
        ChatMessage chatMessage2 = getChatMessage2();

        //when
        chatMessageService.checkExistChatRoom(1L);
        chatMessageService.saveMassage(1L, chatMessage1);
        chatMessageService.saveMassage(1L, chatMessage2);

        //then
        Optional<ChatRoom> noRoom = chatRoomRepository.findById(2L);
        ChatRoom chatRoom1 = chatRoomRepository.findById(1L).orElseGet(ChatRoom::new);
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        assertThat(noRoom).isEmpty();
        assertThat(chatRooms).hasSize(1);
        assertThat(chatRoom1.getRoomId()).isEqualTo(1L);
        assertThat(chatRoom1.getMessageList()).hasSize(2);
        assertThat(chatRoom1.getMessageList())
            .extracting(
                "senderId", "receiverId", "senderNickname", "receiverNickname", "roomId", "content"
            )
            .containsAnyOf(
                tuple(2L, 1L, "bbbb", "aaa", 1L, "testest"),
                tuple(2L, 1L, "aaa", "bbbb", 1L, "testest11111")
            );
    }

    @DisplayName("메시지를 조회할 수 있다.")
    @Test
    void test3() throws Exception {
        //given
        ChatMessage message = getChatMessage1();
        chatMessageService.saveMassage(1L, message);

        //when
        List<ChatMessage> chatMessages = chatMessageService.findChatMessages(1L);

        //then
        assertThat(chatMessages).hasSize(1);
        assertThat(chatMessages)
            .extracting(
                "senderId", "receiverId", "senderNickname", "receiverNickname", "roomId", "content"
            )
            .contains(tuple(2L, 1L, "bbbb", "aaa", 1L, "testest"));
    }

    @DisplayName("메시지 조회시 채팅방이 없다면 예외를 던진다.")
    @Test
    void test4() throws Exception {
        //given//when
        ChatMessage message = getChatMessage1();
        chatMessageService.saveMassage(1L, message);
        //then
        assertThatThrownBy(() -> chatMessageService.findChatMessages(2L))
            .isInstanceOf(WebSocketBusinessException.class)
            .hasMessageContaining(WebSocketExceptionCode.NO_CHAT_ROOM.getMsg());
    }

    private ChatMessage getChatMessage1() {
        return ChatMessage.builder()
            .roomId(1L)
            .content("testest")
            .receiverNickname("aaa")
            .senderId(2L)
            .senderNickname("bbbb")
            .receiverId(1L)
            .build();
    }

    private ChatMessage getChatMessage2() {
        return ChatMessage.builder()
            .roomId(1L)
            .content("testest11111")
            .receiverNickname("bbbb")
            .senderId(1L)
            .senderNickname("aaa")
            .receiverId(2L)
            .build();
    }
}