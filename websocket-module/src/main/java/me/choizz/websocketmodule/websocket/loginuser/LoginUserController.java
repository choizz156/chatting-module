package me.choizz.websocketmodule.websocket.loginuser;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.dto.LoginUserDto;
import me.choizz.chattingmongomodule.user.LoginUser;
import me.choizz.chattingmongomodule.user.LoginUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
@Controller
public class LoginUserController {

    private final LoginUserService loginUserService;

    @MessageMapping("/login-users")
    @SendTo("/public")
    public LoginUserDto addUser(@Payload LoginUserDto loginUserDto) {
        log.warn("{}", loginUserDto);
        LoginUser loginUser = loginUserDto.toEntity();
        loginUserService.connectUser(loginUser);
        return loginUserDto;
    }

    //todo: 페이지네이션

    @GetMapping("/login-users")
    public ResponseEntity<List<LoginUser>> getConnectedUsers() {
        List<LoginUser> connectedUsers = loginUserService.findConnectedUsers();
        log.error("{}", connectedUsers);
        return ResponseEntity.ok(connectedUsers);
    }
}
