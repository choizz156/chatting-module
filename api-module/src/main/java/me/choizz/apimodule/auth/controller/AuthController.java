package me.choizz.apimodule.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.dto.ApiResponseDto;
import me.choizz.apimodule.auth.dto.LoginDto;
import me.choizz.apimodule.auth.service.AuthService;
import me.choizz.apimodule.auth.service.LoginService;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.domainjpamodule.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final LoginService loginService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<Boolean> checkAuth(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        authService.checkAuth(session);

        return ResponseEntity.ok(true);
    }

    @PostMapping("/login")
    public ApiResponseDto<LoginUser> login(
        HttpServletRequest request,
        @RequestBody LoginDto loginDto
    ) {

        User user = loginService.login(loginDto.email(), loginDto.password());
        HttpSession session = request.getSession();

        if (loginService.isExistSession(loginDto.email(), session)) {
            session = request.getSession();
        }

        LoginUser loginUser = loginService.createSession(session, user);
        return new ApiResponseDto<>(loginUser);
    }

    @DeleteMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        loginService.logout(session);
        return "로그아웃";
    }
}
