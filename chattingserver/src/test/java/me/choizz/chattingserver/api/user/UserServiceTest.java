package me.choizz.chattingserver.api.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import me.choizz.chattingserver.api.controller.user.dto.JoinDto;
import me.choizz.chattingserver.api.service.UserService;
import me.choizz.chattingserver.domain.user.User;
import me.choizz.chattingserver.domain.user.UserRepository;
import me.choizz.chattingserver.domain.user.UserRole;
import me.choizz.chattingserver.api.exception.BusinessLoginException;
import me.choizz.chattingserver.api.exception.ExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @DisplayName("회원 가입을 할 수 있다.")
    @Test
    void join() throws Exception {
        //given
        JoinDto dto = new JoinDto("test@gmail.com", "testdfd11", "test");

        //when
        User user = userService.join(dto);

        //then
        assertThat(userRepository.count()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo(dto.email());
        assertThat(user.getNickname()).isEqualTo(dto.nickname());
        assertThat(user.getRoles()).isEqualTo(UserRole.USER);
    }

    @DisplayName("회원 가입 시 이메일이 중복되는 경우 예외를 던진다.")
    @Test
    void duplication_email() throws Exception {
        //given
        JoinDto dto = new JoinDto("test@gmail.com", "testdfdf11", "test1");
        userService.join(dto);

        //when
        JoinDto dto2 = new JoinDto("test@gmail.com", "testdfd231", "test2");

        //then
        assertThatCode(() -> userService.join(dto2))
            .isInstanceOf(BusinessLoginException.class)
            .hasMessageContaining(ExceptionCode.EXIST_EMAIL.getMsg());
    }

    @DisplayName("회원 가입 시 닉네임이 중복되는 경우 예외를 던진다.")
    @Test
    void duplication_nickname() throws Exception {
        //given
        JoinDto dto = new JoinDto("test@gmail.com", "testdfdf11", "test1");
        userService.join(dto);

        //when
        JoinDto dto2 = new JoinDto("test2@gmail.com", "testdfd231", "test1");

        //then
        assertThatCode(() -> userService.join(dto2))
            .isInstanceOf(BusinessLoginException.class)
            .hasMessageContaining(ExceptionCode.EXIST_NICKNAME.getMsg());
    }

    @DisplayName("비밀번호 암호화")
    @Test
    void pwd() throws Exception {
        //given
        String pwd = "12345asd";

        //when
        String result = passwordEncoder.encode(pwd);

        //then
        assertThat(passwordEncoder.matches(pwd, result)).isTrue();

    }

}