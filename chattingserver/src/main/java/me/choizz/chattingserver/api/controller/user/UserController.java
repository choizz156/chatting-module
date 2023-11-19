package me.choizz.chattingserver.api.controller.user;

import static net.logstash.logback.argument.StructuredArguments.kv;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.choizz.chattingserver.api.ApiResponseDto;
import me.choizz.chattingserver.api.controller.user.dto.JoinDto;
import me.choizz.chattingserver.api.service.user.UserService;
import me.choizz.chattingserver.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

//    private static final Logger fileLog = LoggerFactory.getLogger("fileLog");
    private static final Logger jsonLog = LoggerFactory.getLogger("jsonLog");

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseDto<String> join(@RequestBody @Valid JoinDto joinDto) {

        User user = userService.join(joinDto);

        jsonLog.info("{}, {}",
            kv("이메일", joinDto.email()),
            kv("닉네임", joinDto.nickname())
        );

        return new ApiResponseDto<>(user.getEmail());
    }

}
