package me.choizz.apimodule.api.controller.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.choizz.apimodule.auth.dto.LoginDto;
import me.choizz.domainjpamodule.dto.JoinDto;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import me.choizz.domainjpamodule.user.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUpEach() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원 가입을 한다.")
    @Test
    void test1() throws Exception {
        //given
        JoinDto dto = new JoinDto("test@gmail.com", "testdfd11", "test");

        mockMvc.perform(
                post("/users")
                    .content(objectMapper.writeValueAsBytes(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.email").value("test@gmail.com"))
            .andExpect(jsonPath("$.data.nickname").value("test"))
            .andDo(print());
    }

    @Nested
    @DisplayName("dto 유효성 검사")
    class ValidationTest {

        @DisplayName("회원가입시 이메일은 공백이 아니어야 한다.")
        @Test
        void test2() throws Exception {
            //given
            JoinDto dto = new JoinDto("", "testdfd11", "test");

            mockMvc.perform(
                    post("/users")
                        .content(objectMapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.customFieldErrors[0].field").value("email"))
                .andExpect(jsonPath("$.data.customFieldErrors[0].rejectedValue").value(""))
                .andExpect(jsonPath("$.data.customFieldErrors[0].reason").value("이메일은 공백일 수 없습니다."))
                .andDo(print());
        }

        @DisplayName("회원가입시 이메일은 @를 포함해야 합니다.")
        @Test
        void test5() throws Exception {
            //given
            JoinDto dto = new JoinDto("resfdf.com", "testdfd11", "test");

            mockMvc.perform(
                    post("/users")
                        .content(objectMapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.customFieldErrors[0].field").value("email"))
                .andExpect(
                    jsonPath("$.data.customFieldErrors[0].rejectedValue").value("resfdf.com"))
                .andExpect(jsonPath("$.data.customFieldErrors[0].reason").value("@를 포함해야합니다."))
                .andDo(print());
        }

        @DisplayName("회원 가입시 비밀번호는 값이 입력돼야 합니다.")
        @Test
        void test3() throws Exception {
            //given
            JoinDto dto = new JoinDto("re@sfdf.com", " ", "test");

            mockMvc.perform(
                    post("/users")
                        .content(objectMapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.customFieldErrors[0].field").value("password"))
                .andExpect(jsonPath("$.data.customFieldErrors[0].rejectedValue").value(" "))
                .andExpect(
                    jsonPath("$.data.customFieldErrors[0].reason").value("must not be blank"))
                .andDo(print());
        }

        @DisplayName("회원 가입시 닉네임은 값이 입력돼야 합니다.")
        @Test
        void test4() throws Exception {
            //given
            JoinDto dto = new JoinDto("re@sfdf.com", "1233", " ");

            mockMvc.perform(
                    post("/users")
                        .content(objectMapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.customFieldErrors[0].field").value("nickname"))
                .andExpect(jsonPath("$.data.customFieldErrors[0].rejectedValue").value(" "))
                .andExpect(
                    jsonPath("$.data.customFieldErrors[0].reason").value("must not be blank")
                )
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("인증 테스트")
    class AuthTest {

        @Autowired
        UserService userService;

        @DisplayName("로그인")
        @Test
        void test4() throws Exception {
            //given
            JoinDto joinDto = new JoinDto("test@gmail.com", "1233", "test");
            User join = userService.join(joinDto);

            LoginDto loginDto = new LoginDto("test@gmail.com", "1233");
            mockMvc.perform(
                    post("/auth/login")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.nickname").value("test"))
                .andExpect(jsonPath("$.data.userId").value(Matchers.notNullValue()))
                .andDo(print());
        }

        @DisplayName("로그아웃")
        @WithMockUser
        @Test
        void test3() throws Exception {
            //given
            mockMvc.perform(get("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("로그아웃"))
                .andDo(print());
        }

        @DisplayName("로그인 유저는 권한 인증을 할 수 잇다.")
        @WithMockUser
        @Test
        void test6() throws Exception {
            //given
            mockMvc.perform(get("/auth"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(print());
        }
    }
}