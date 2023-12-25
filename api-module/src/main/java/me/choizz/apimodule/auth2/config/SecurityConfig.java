package me.choizz.apimodule.auth2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.choizz.apimodule.auth2.UserDetailsVerification;
import me.choizz.apimodule.auth2.filter.EmailPasswordAuthFilter;
import me.choizz.apimodule.auth2.handler.AuthDeniedHandler;
import me.choizz.apimodule.auth2.handler.AuthEntryPointHandler;
import me.choizz.apimodule.auth2.handler.AuthFailureHandler;
import me.choizz.apimodule.auth2.handler.AuthSuccessHandler;
import me.choizz.chattingredismodule.session.LoginUsers;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final UserDetailsVerification userDetailsVerification;
    private final LoginUsers loginUsers;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(new CorsConfig().corsFilter()))
            .sessionManagement(s -> s.sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                .sessionFixation()
                .changeSessionId()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("http://localhost:3000/login")
            )
            .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(e -> {
                e.accessDeniedHandler(new AuthDeniedHandler(objectMapper));
                e.authenticationEntryPoint(new AuthEntryPointHandler(objectMapper));
            })
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(HttpMethod.GET, "/auth").hasRole("USER");
                auth.requestMatchers("/chatting-rooms", "/messages/**").hasRole("USER");
                auth.anyRequest().permitAll();
            });

        return http.build();
    }

    @Bean
    public EmailPasswordAuthFilter emailPasswordAuthFilter() {

        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new AuthSuccessHandler(objectMapper, loginUsers));
        filter.setAuthenticationFailureHandler(new AuthFailureHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());

        return filter;
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {

        ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlStrategy
            = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        concurrentSessionControlStrategy.setMaximumSessions(1);
        concurrentSessionControlStrategy.setExceptionIfMaximumExceeded(false);

        return
            new CompositeSessionAuthenticationStrategy(
                List.of(
                    concurrentSessionControlStrategy,
                    new RegisterSessionAuthenticationStrategy(sessionRegistry()),
                    new ChangeSessionIdAuthenticationStrategy()
                ));
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsVerification);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }
}

