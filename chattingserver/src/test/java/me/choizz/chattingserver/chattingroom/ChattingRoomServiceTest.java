package me.choizz.chattingserver.chattingroom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import me.choizz.chattingserver.api.exception.BusinessLogicException;
import me.choizz.chattingserver.api.service.chattingroom.ChattingRoomService;
import me.choizz.chattingserver.domain.chattingroom.ChattingRoomRepository;
import me.choizz.chattingserver.domain.user.User;
import me.choizz.chattingserver.domain.user.UserRepository;
import me.choizz.chattingserver.domain.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChattingRoomServiceTest {

    @Autowired
    private ChattingRoomRepository chattingRoomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChattingRoomService chattingRoomService;

    private User host;
    private User client;

    @BeforeEach
    void setUpEach() {
        chattingRoomRepository.deleteAll();
        userRepository.deleteAll();
        var host = new User("host@gmail.com", "1234", "host", UserRole.USER);
        var client = new User("client@gmail.com", "12346", "client", UserRole.USER);
        this.host = userRepository.save(host);
        this.client = userRepository.save(client);
    }

    @DisplayName("채팅방을 생성할 수 있다.")
    @Test
    void test1() throws Exception {
        //given //when
        var result =
            chattingRoomService.createOneToOne("testRoom", host.getId(), client.getId());

        //then
        assertThat(result.getRoomName()).isEqualTo("testRoom");
        assertThat(result.getHost().getEmail()).isEqualTo("host@gmail.com");
        assertThat(result.getClient().getEmail()).isEqualTo("client@gmail.com");
    }

    @DisplayName("1대1 채팅방에 참여하고 있는 유저 2명이 동일한 채팅방 생성 시도시 예외를 던진다.")
    @Test
    void test2() throws Exception {
        //given //when
        chattingRoomService.createOneToOne("testRoom", host.getId(), client.getId());
        //then
        assertThatThrownBy(
            () -> chattingRoomService.createOneToOne("testRoom2", host.getId(), client.getId())
        )
            .isInstanceOf(BusinessLogicException.class);
    }

}
