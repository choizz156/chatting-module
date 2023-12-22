package me.choizz.apimodule.api.controller.profile;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final Environment environment;

    @GetMapping("/profile")
    public String profile() {
        List<String> activeProfiles = List.of(environment.getActiveProfiles());
        String defaultProfile = activeProfiles.isEmpty() ? "default" : activeProfiles.get(0);
        return activeProfiles.stream()
            .filter(p -> p.contains("prod"))
            .findAny()
            .orElse(defaultProfile);
    }

}
