package me.choizz.apimodule.api.controller.chattingroom;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.choizz.domainjpamodule.chattingroom.ChattingRoom;
import me.choizz.domainjpamodule.chattingroom.ChattingRoomService;
import me.choizz.domainjpamodule.dto.ChatRoomRequestDto;
import me.choizz.domainjpamodule.user.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChattingRoomController.class)
class ChattingRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChattingRoomService chattingRoomService;


    @WithMockUser
    @DisplayName("채팅룸을 생성한다.")
    @Test
    void test1() throws Exception {
        //given
        ChattingRoom testRoom = new ChattingRoom("testRoom");
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

        testRoom.makeChattingRoom(user1, user2);
        Mockito.when(chattingRoomService.createOneToOne(anyString(), anyLong(), anyLong()))
            .thenReturn(testRoom);

        //when then
        ChatRoomRequestDto dto = new ChatRoomRequestDto("testRoom",1L , 2L);
        mockMvc.perform(
                post("/chatting-rooms")
                    .content(objectMapper.writeValueAsBytes(dto))
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.hostNickname").value("test1"))
            .andExpect(jsonPath("$.data.clientNickname").value("test2"))
            .andDo(print());
    }

    @Nested
    @DisplayName("chatroom dto 유효성 검사")
    class ValidationTest {

        @DisplayName("hostId는 null이 아니어야 합니다.")
        @WithMockUser
        @Test
        void test2() throws Exception {
            //given
            ChatRoomRequestDto dto = new ChatRoomRequestDto("testRoom", null, 2L);

            mockMvc.perform(
                    post("/chatting-rooms")
                        .content(objectMapper.writeValueAsBytes(dto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.customFieldErrors[0].field").value("hostId"))
                .andExpect(jsonPath("$.data.customFieldErrors[0].rejectedValue").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.data.customFieldErrors[0].reason").value("must not be null"))
                .andDo(print());
        }
        @DisplayName("clietid는 null이 아니어야 합니다.")
        @WithMockUser
        @Test
        void test3() throws Exception {
            //given
            ChatRoomRequestDto dto = new ChatRoomRequestDto("testRoom", 1L, null );

            mockMvc.perform(
                    post("/chatting-rooms")
                        .content(objectMapper.writeValueAsBytes(dto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.customFieldErrors[0].field").value("clientId"))
                .andExpect(jsonPath("$.data.customFieldErrors[0].rejectedValue").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.data.customFieldErrors[0].reason").value("must not be null"))
                .andDo(print());
        }
    }
}