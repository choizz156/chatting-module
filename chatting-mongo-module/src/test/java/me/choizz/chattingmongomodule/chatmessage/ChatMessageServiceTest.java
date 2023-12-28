package me.choizz.chattingmongomodule.chatmessage;

import static org.assertj.core.api.Assertions.assertThat;

import me.choizz.chattingmongomodule.chatRoom.ChatRoom;
import me.choizz.chattingmongomodule.chatRoom.ChatRoomRepository;
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

    @DisplayName("chatRoom 도큐먼트가 존재하지 않는다면 생성한다.")
    @Test
    void test1() throws Exception {
        chatMessageService.checkExistChatRoom1(1L);

        ChatRoom result = chatRoomRepository.findById(1L).orElseGet(ChatRoom::new);
        assertThat(result.getRoomId()).isEqualTo(1L);
    }
}