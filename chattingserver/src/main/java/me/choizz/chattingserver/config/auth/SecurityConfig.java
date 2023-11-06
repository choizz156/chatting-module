package me.choizz.chattingserver.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            )
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(new CorsConfig().corsFilter()));

        http.
            formLogin(form -> {
                    form.loginProcessingUrl("users/auth");
                    form.loginPage("/login-form");
                    form.failureForwardUrl("/login-form");
                    form.successForwardUrl("/");
                    form.failureForwardUrl("/login-form");
                }
            )
            .logout(logout -> logout.logoutSuccessUrl("/"));

        http.
            exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"));

        http.
            authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
