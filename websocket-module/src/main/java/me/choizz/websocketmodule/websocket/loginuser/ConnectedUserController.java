package me.choizz.websocketmodule.websocket.loginuser;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.dto.ConnectedUserDto;
import me.choizz.chattingmongomodule.user.ConnectedUser;
import me.choizz.chattingmongomodule.user.ConnectedUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ConnectedUserController {

    private final ConnectedUserService connectedUserService;

    @MessageMapping("/login-users")
    @SendTo("/public")
    public ConnectedUserDto addUser(@Payload ConnectedUserDto connectedUserDto) {
        log.warn("{}", connectedUserDto);
        ConnectedUser connectedUser = connectedUserDto.toEntity();
        connectedUserService.connectUser(connectedUser);
        return connectedUserDto;
    }

    @DeleteMapping ("/logout/{userId}")
    public void deleteLoginUser(@PathVariable("userId") Long userId){
        connectedUserService.disconnectUser(userId);
    }

    @GetMapping("/login-users")
    public ResponseEntity<List<ConnectedUser>> getConnectedUsers() {
        List<ConnectedUser> connectedUsers = connectedUserService.findConnectedUsers();
        return ResponseEntity.ok(connectedUsers);
    }
}
