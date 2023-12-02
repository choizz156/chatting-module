package me.choizz.apimodule.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.choizz.apimodule.auth.details.UserVerificationService;
import me.choizz.apimodule.auth.handler.AuthDeniedHandler;
import me.choizz.apimodule.auth.handler.AuthEntryPointHandler;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig {

    private static final String URL = "http://localhost:3000";
    private final UserVerificationService userVerificationService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
            .sessionManagement(
                session ->
                    session.maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .sessionRegistry(sessionRegistry())
            )
//                        .expiredSessionStrategy(new CustomSessionExpiredStrategy())

            .cors(cors -> cors.configurationSource(new CorsConfig().corsFilter()))
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .apply(new FilterConfig(objectMapper, userRepository, userVerificationService));

        http.
            exceptionHandling(exception -> {
                    exception.accessDeniedHandler(new AuthDeniedHandler(objectMapper));
                    exception.authenticationEntryPoint(new AuthEntryPointHandler(objectMapper));
                }
            );

        http.authorizeHttpRequests(
            auth ->
                auth.anyRequest().permitAll()
        );

        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

//
//    @Bean
//    public static ServletListenerRegistrationBean<?> httpSessionEventPublisher() { //상태 유지를 하지 않음
//        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
//    }
}
