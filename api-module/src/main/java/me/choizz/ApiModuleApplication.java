package me.choizz;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@RequiredArgsConstructor
@EnableScheduling
@SpringBootApplication(scanBasePackages = {
    "me.choizz.apimodule",
    "me.choizz.domainjpamodule",
    "me.choizz.websocketmodule",
    "me.choizz.chattingmongomodule",
    "me.choizz.chattingredismodule"
})
public class ApiModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiModuleApplication.class, args);
    }
}
