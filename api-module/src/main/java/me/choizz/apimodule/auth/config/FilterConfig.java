package me.choizz.apimodule.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.choizz.apimodule.auth.UserVerificationService;
import me.choizz.apimodule.auth.filter.EmailPasswordAuthenticationFilter;
import me.choizz.apimodule.auth.handler.AuthFailureHandler;
import me.choizz.apimodule.auth.handler.AuthSuccessHandler;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserVerificationService userVerificationService;

    @Bean
    public EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter() {
        EmailPasswordAuthenticationFilter filter =
            new EmailPasswordAuthenticationFilter("/login", objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(
            new AuthSuccessHandler(userRepository, objectMapper)
        );
        filter.setAuthenticationFailureHandler(new AuthFailureHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userVerificationService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
