package me.choizz.apimodule.api.controller.chatmessage;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;
import me.choizz.chattingmongomodule.chatmessage.ChatMessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageService chatMessageService;

    @DisplayName("채팅 내역을 조회할 수 있다.")
    @WithMockUser
    @Test
    void test1() throws Exception {
        //given
        ChatMessage message1 = ChatMessage.builder()
            .receiverId(1L)
            .receiverNickname("test1")
            .senderNickname("test2")
            .senderId(2L)
            .content("테스트내용입니다.")
            .roomId(1L)
            .build();

        ChatMessage message2 = ChatMessage.builder()
            .receiverId(2L)
            .receiverNickname("test2")
            .senderNickname("test1")
            .senderId(1L)
            .content("테스트내용입니다.2")
            .roomId(1L)
            .build();

        List<ChatMessage> messages = new ArrayList<>(List.of(message1, message2));
        Mockito.when(chatMessageService.findChatMessages(anyLong())).thenReturn(messages);

        //when//then
        mockMvc.perform(
                get("/messages/1")
            )
            .andExpect(status().isOk())
            //data0
            .andExpect(jsonPath("$.data[0].content").value("테스트내용입니다."))
            .andExpect(jsonPath("$.data[0].roomId").value(1))
            .andExpect(jsonPath("$.data[0].senderId").value(2))
            .andExpect(jsonPath("$.data[0].receiverId").value(1))
            .andExpect(jsonPath("$.data[0].senderNickname").value("test2"))
            .andExpect(jsonPath("$.data[0].receiverNickname").value("test1"))
            .andExpect(jsonPath("$.data[0].roomId").value(1))
            //data1
            .andExpect(jsonPath("$.data[1].content").value("테스트내용입니다.2"))
            .andExpect(jsonPath("$.data[1].senderId").value(1))
            .andExpect(jsonPath("$.data[1].receiverId").value(2))
            .andExpect(jsonPath("$.data[1].senderNickname").value("test1"))
            .andExpect(jsonPath("$.data[1].receiverNickname").value("test2"))
            .andExpect(jsonPath("$.data[1].roomId").value(1))
            .andDo(print());
    }

}