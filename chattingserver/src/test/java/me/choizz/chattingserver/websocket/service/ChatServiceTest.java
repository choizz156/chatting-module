package me.choizz.chattingserver.websocket.service;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ChatServiceTest {

//    @Autowired
//    private ChatMessageRepository chatMessageRepository;
//
//    @Autowired
//    private ChatRoomRepository chattingRoomRepository;
//
//    @Autowired
//    private ChatService chatService;
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @BeforeEach
//    void setUpEach() {
//        chattingRoomRepository.deleteAll();
//        chattingRoomRepository.deleteAll();
//    }
//
//    @DisplayName("채팅룸을 생성한다.")
//    @Test
//    void chatRoom() throws Exception {
//        //given
//        chatService.createChattingRoom("testRoom");
//
//        //when
//        ChattingRoom result = chattingRoomRepository.findAll().get(0);
//
//        //then
//        assertThat(result.getName()).isEqualTo("testRoom");
//        assertThat(result.getId()).isNotBlank();
//        assertThat(result.getCreatedAt()).isNotNull();
//    }
//
//    @DisplayName("채팅 내역이 저장된다.")
//    @Test
//    void msg() throws Exception {
//        //given
//        var room = chatService.createChattingRoom("testRoom");
//
//        List<ChatInfo> chatList = List.of(
//            new ChatInfo("test", "testmsg1"),
//            new ChatInfo("test", "testmsg2"),
//            new ChatInfo("test", "testmsg3")
//        );
//
//        LocalDateTime now = LocalDateTime.of(2022, 11, 12, 11, 11, 11);
//        List<ChatMessage> list = chatList.stream().map(m -> m.toEntity(now)).toList();
//
//        //when
//        list.forEach(c -> chatService.saveMassage(room.getId(), c));
//
//        //then
//        ChattingRoom result = chattingRoomRepository.findAll().get(0);
//        assertThat(result.getMessages())
//            .extracting("nickname", "message", "createdAt")
//            .containsAnyOf(
//                tuple("test", "testmsg1", now),
//                tuple("test", "testmsg2", now),
//                tuple("test", "testmsg3", now)
//            );
//    }
//
//
    @Profile("test")
    @TestConfiguration
    static class EmbeddedAutoConfig extends EmbeddedMongoAutoConfiguration {

    }
}