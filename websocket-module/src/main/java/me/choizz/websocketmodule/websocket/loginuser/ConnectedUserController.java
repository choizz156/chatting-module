package me.choizz.websocketmodule.websocket.loginuser;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.dto.ConnectedUserDto;
import me.choizz.chattingmongomodule.user.ConnectedUser;
import me.choizz.chattingmongomodule.user.ConnectedUserService;
import me.choizz.websocketmodule.websocket.exception.ResponseDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ConnectedUserController {

    private final ConnectedUserService connectedUserService;
    private final SimpMessageSendingOperations operations;

    @ResponseStatus(CREATED)
    @PostMapping("/login-users")
    public ResponseDto<ConnectedUserDto> addUser(@RequestBody ConnectedUserDto dto) {
        ConnectedUser connectedUser = dto.toEntity();
        connectedUserService.connectUser(connectedUser);
        return new ResponseDto<>(dto);
    }

    @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    @MessageMapping("/connected-users")
    public void getConnectedUsers() {
        List<ConnectedUser> connectedUsers = connectedUserService.findConnectedUsers();
        operations.convertAndSend("/topic/public", connectedUsers);
    }

    @DeleteMapping ("/disconnect/{userId}")
    public void deleteLoginUser(@PathVariable("userId") Long userId){
        connectedUserService.disconnectUser(userId);
    }
}
