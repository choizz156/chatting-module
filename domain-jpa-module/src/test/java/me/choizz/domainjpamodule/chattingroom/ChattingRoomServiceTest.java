package me.choizz.domainjpamodule.chattingroom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import me.choizz.domainjpamodule.exception.ApiBusinessLogicException;
import me.choizz.domainjpamodule.exception.ApiExceptionCode;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "me.choizz.domainjpamodule")
@EntityScan("me.choizz.domainjpamodule")
@ContextConfiguration(
    classes = {ChattingRoomRepository.class, ChattingRoomService.class, UserRepository.class})
@SpringBootTest
class ChattingRoomServiceTest {

    @Autowired
    ChattingRoomRepository chattingRoomRepository;
    @Autowired
    ChattingRoomService chattingRoomService;
    @Autowired
    UserRepository userRepository;

    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUpEach() {
        createUsers();
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

    @DisplayName("기존에 방이 존재하면 그 방을 리턴한다.")
    @Test
    void test1() throws Exception {
        //given//when
        ChattingRoom testRoom =
            chattingRoomService.createOneToOne("testRoom", user2.getId(), user1.getId());
        //then
        assertThat(testRoom.getRoomName()).isEqualTo("testRoom");
        assertThat(testRoom.getHost()).isEqualTo(user1);
        assertThat(testRoom.getClient()).isEqualTo(user2);
    }

    @DisplayName("기존에 방이 존재하지 않으면 새로운 방을 생성한다.")
    @Test
    void test2() throws Exception {
        //given//when
        ChattingRoom testRoom2 =
            chattingRoomService.createOneToOne("testRoom2", user3.getId(), user1.getId());
        //then
        assertThat(testRoom2.getRoomName()).isEqualTo("testRoom2");
        assertThat(testRoom2.getHost()).isEqualTo(user3);
        assertThat(testRoom2.getClient()).isEqualTo(user1);
    }

    @DisplayName("존재하지 않는 유저일 시 예외를 던진다.")
    @Test
    void test3() throws Exception {
        //when//then
        Long id = user1.getId();
        assertThatThrownBy(() -> chattingRoomService.createOneToOne("test2", id, 5L))
            .isInstanceOf(ApiBusinessLogicException.class)
            .hasMessageContaining(ApiExceptionCode.NOT_FOUND_UER.getMsg());
    }

    private void createUsers() {
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
        user3 = User.builder()
            .nickname("test3")
            .email("test3@gmail.com")
            .password("1234")
            .build();
    }

    private void saveUsers() {
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);
    }
}