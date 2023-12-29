package me.choizz.domainjpamodule.chattingroom;

import static org.assertj.core.api.Assertions.assertThat;

import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "me.choizz.domainjpamodule")
@EntityScan("me.choizz.domainjpamodule")
@ContextConfiguration(classes = {ChattingRoomRepository.class, UserRepository.class})
@DataJpaTest
class ChattingRoomRepositoryTest {

    @Autowired
    ChattingRoomRepository chattingRoomRepository;
    @Autowired
    UserRepository userRepository;

    User user1;
    User user2;

    @BeforeEach
    void setUpEach() {
        saveUsers();
        ChattingRoom testRoom = new ChattingRoom("testRoom");
        testRoom.makeChattingRoom(user1, user2);
        chattingRoomRepository.save(testRoom);
    }

    @AfterEach
    void tearDown() {
        chattingRoomRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    private void saveUsers() {
        user1 = User.builder()
            .nickname("test1")
            .email("test1@gmail.com")
            .password("1234")
            .build();

        user2 = User.builder()
            .nickname("test2")
            .email("test2@gmail.com")
            .password("1234")
            .build();
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
    }

    @Test
    void findChattingRoomByHostIdAndClientId() throws Exception {
        //given//when
        ChattingRoom chattingRoom =
            chattingRoomRepository
                .findChattingRoomByHostIdAndClientId(user1.getId(), user2.getId())
                .orElseGet(ChattingRoom::new);

        //then
        assertThat(chattingRoom.getId()).isEqualTo(1L);
        assertThat(chattingRoom.getRoomName()).isEqualTo("testRoom");
        assertThat(chattingRoom.getHost()).isEqualTo(user1);
        assertThat(chattingRoom.getClient()).isEqualTo(user2);
    }
}