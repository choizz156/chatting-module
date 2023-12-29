package me.choizz.apimodule.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "me.choizz.domainjpamodule")
@EntityScan("me.choizz.domainjpamodule")
public class JpaScanConfig {
}
