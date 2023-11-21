package me.choizz.apimodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "me.choizz.apimodule",
    "me.choizz.domainjpamodule"
})
@EnableJpaRepositories(basePackages = "me.choizz.domainjpamodule")
@EntityScan("me.choizz.domainjpamodule")
public class ApiModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiModuleApplication.class, args);
    }

}
