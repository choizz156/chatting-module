package me.choizz.websocketmodule.websocket.loginuser;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.chattingredismodule.session.LoginUsers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ConnectedUserController {

    private final SimpMessageSendingOperations operations;
    private final LoginUsers loginUsers;

    @Scheduled(fixedDelay = 3000, initialDelay = 200)
    public void getConnectedUsers() {
        Set<LoginUser> connectedUsers = this.loginUsers.get();
        operations.convertAndSend("/topic/public", connectedUsers);
    }

    @MessageExceptionHandler
    @SendTo("/topic/error")
    public String handler(Exception e) {
        return "통신 장애";
    }
}
