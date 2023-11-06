package me.choizz.chattingserver.api.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.api.ApiResponseDto;
import me.choizz.chattingserver.api.controller.user.dto.JoinDto;
import me.choizz.chattingserver.api.service.UserService;
import me.choizz.chattingserver.domain.user.User;
import me.choizz.chattingserver.domain.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseDto<String> join(@RequestBody @Valid JoinDto joinDto){
        User user = userService.join(joinDto);
        return new ApiResponseDto<>(user.getEmail());
    }

}
