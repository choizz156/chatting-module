package me.choizz.chattingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChattingserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChattingserverApplication.class, args);
    }

}
