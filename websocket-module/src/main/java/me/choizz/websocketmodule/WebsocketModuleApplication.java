package me.choizz.websocketmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {
        "me.choizz.websocketmodule",
        "me.choizz.chattingmongomodule"
    }
)
public class WebsocketModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketModuleApplication.class, args);
    }

}
