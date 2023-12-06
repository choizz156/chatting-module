package me.choizz.apimodule.api.controller.connecteduser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.dto.ApiResponseDto;
import me.choizz.chattingmongomodule.dto.ConnectedUserDto;
import me.choizz.chattingmongomodule.user.ConnectedUser;
import me.choizz.chattingmongomodule.user.ConnectedUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ConnectedController {

    private final ConnectedUserService connectedUserService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/login-users")
    public ApiResponseDto<ConnectedUserDto> addUser(@RequestBody ConnectedUserDto dto) {
        ConnectedUser connectedUser = dto.toEntity();
        connectedUserService.connectUser(connectedUser);
        return new ApiResponseDto<>(dto);
    }

    @DeleteMapping("/logout/{userId}")
    public void deleteLoginUser(@PathVariable("userId") Long userId){
        connectedUserService.disconnectUser(userId);
    }
}
