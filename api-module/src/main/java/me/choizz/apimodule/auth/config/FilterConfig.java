package me.choizz.apimodule.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.auth.details.UserVerificationService;
import me.choizz.apimodule.auth.filter.EmailPasswordAuthenticationFilter;
import me.choizz.apimodule.auth.handler.AuthFailureHandler;
import me.choizz.apimodule.auth.handler.AuthSuccessHandler;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FilterConfig extends AbstractHttpConfigurer<FilterConfig, HttpSecurity> {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserVerificationService userVerificationService;


    @Override
    public void configure(final HttpSecurity builder) throws Exception {
        EmailPasswordAuthenticationFilter filter =
            new EmailPasswordAuthenticationFilter("/login", objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(
            new AuthSuccessHandler(userRepository, objectMapper)
        );
        filter.setAuthenticationFailureHandler(new AuthFailureHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        builder.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
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
