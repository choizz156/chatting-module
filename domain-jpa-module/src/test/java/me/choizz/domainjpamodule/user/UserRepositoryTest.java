package me.choizz.domainjpamodule.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@EnableAutoConfiguration
@ContextConfiguration(classes = UserRepository.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUpEach() {
        saveUsers();
    }

    private void saveUsers() {
        User user1 = User.builder()
            .nickname("test1")
            .email("test1@gmail.com")
            .password("1234")
            .build();
        User user2 = User.builder()
            .nickname("test2")
            .email("test2@gmail.com")
            .password("1234")
            .build();

       userRepository.save(user1);
       userRepository.save(user2);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    void findByEmail() throws Exception {
        //given//when
        User result = userRepository.findByEmail("test1@gmail.com").orElseGet(User::new);

        //then
        assertThat(result.getEmail()).isEqualTo("test1@gmail.com");
        assertThat(result.getNickname()).isEqualTo("test1");
    }

    @Test
    void existsUserByNickname() throws Exception {
        //given//when//then
        assertThat(userRepository.existsUserByNickname("test1")).isTrue();
    }

    @Test
    void existsUserByEmail() throws Exception {
        //given//when//then
        assertThat(userRepository.existsUserByEmail("test1@gmail.com")).isTrue();
    }
}