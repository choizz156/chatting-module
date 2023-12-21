package me.choizz.apimodule;

import jakarta.annotation.PreDestroy;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
@EnableJpaRepositories(basePackages = "me.choizz.domainjpamodule")
@EntityScan("me.choizz.domainjpamodule")
public class ApiModuleApplication {

    private final RedisTemplate<String, Object> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ApiModuleApplication.class, args);
    }

    @PreDestroy
    public void close(){
        RedisOperations<String, Object> operations = redisTemplate.opsForValue().getOperations();
        Set<String> keys = operations.keys("*");
        operations.delete(keys);
    }
}
