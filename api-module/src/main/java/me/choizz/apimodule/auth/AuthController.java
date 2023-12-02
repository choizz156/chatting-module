package me.choizz.apimodule.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/users/auth")
@Controller
public class AuthController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<Boolean> checkAuth(){
        return ResponseEntity.ok(true);
    }
}
