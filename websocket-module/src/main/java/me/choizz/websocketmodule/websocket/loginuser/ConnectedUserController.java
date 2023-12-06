package me.choizz.websocketmodule.websocket.loginuser;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.user.ConnectedUser;
import me.choizz.chattingmongomodule.user.ConnectedUserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ConnectedUserController {

    private final ConnectedUserService connectedUserService;
    private final SimpMessageSendingOperations operations;


    @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    @MessageMapping("/connected-users")
    public void getConnectedUsers() {
        List<ConnectedUser> connectedUsers = connectedUserService.findConnectedUsers();
        operations.convertAndSend("/topic/public", connectedUsers);
    }
}
