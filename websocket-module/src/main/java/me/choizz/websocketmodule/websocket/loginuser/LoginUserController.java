package me.choizz.websocketmodule.websocket.loginuser;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.user.LoginUser;
import me.choizz.chattingmongomodule.user.LoginUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginUserController {

    private final LoginUserService loginUserService;

    @MessageMapping("/login-user")
    @SendTo("/public")
    public LoginUser addUser(@Payload LoginUser user){
        loginUserService.connectUser(user);
        return user;
    }

    //todo: 페이지네이션
    @GetMapping("/login-users")
    public ResponseEntity<List<LoginUser>> getConnectedUsers() {
        return ResponseEntity.ok(loginUserService.findConnectedUsers());
    }
}
