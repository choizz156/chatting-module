package me.choizz.apimodule.api.controller.user;

import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.dto.ApiResponseDto;
import me.choizz.domainjpamodule.dto.JoinDto;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserService;
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

//    private static final Logger fileLog = LoggerFactory.getLogger("fileLog");
//    private static final Logger jsonLog = LoggerFactory.getLogger("jsonLog");

    private final UserService userService;

    @ResponseStatus(CREATED)
    @PostMapping
    public ApiResponseDto<UserResponseDto> join(@RequestBody @Valid JoinDto joinDto) {
        log.info("{}, {}", joinDto.email(), joinDto.nickname());
        User user = userService.join(joinDto);

        return new ApiResponseDto<>(UserResponseDto.of(user));
    }


}
