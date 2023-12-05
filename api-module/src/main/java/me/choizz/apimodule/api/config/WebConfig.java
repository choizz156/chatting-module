package me.choizz.apimodule.api.config;

import me.choizz.apimodule.auth.filter.LoginDuplicationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new LoginDuplicationInterceptor())
            .order(1)
            .addPathPatterns("/auth/login");
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("*")
            .allowedOriginPatterns("*")
            .allowCredentials(true);
    }
}
