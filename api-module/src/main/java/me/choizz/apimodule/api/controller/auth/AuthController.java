package me.choizz.apimodule.api.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");

    @GetMapping
    public ResponseEntity<Boolean> checkAuth(HttpServletRequest request) {
        logger.info("seesionId =>> {}", request.getSession().getId());
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        logger.info("logout sessionId =>> {}", session.getId());
        session.invalidate();
        return "로그아웃";
    }
}
