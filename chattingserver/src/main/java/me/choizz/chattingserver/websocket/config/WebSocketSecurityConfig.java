package me.choizz.chattingserver.websocket.config;

import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

//    @Bean
//    public AuthorizationManager<Message<?>> messageAuthorizationManager(
//        MessageMatcherDelegatingAuthorizationManager.Builder messages
//    ) {
//        messages
//            .nullDestMatcher().authenticated()
//            .simpSubscribeDestMatchers("/user/queue/errors").permitAll()
//            .simpDestMatchers("/pub/**").hasRole("USER")
//            .simpSubscribeDestMatchers("sub/**").hasRole("USER")
//            .anyMessage().denyAll();
//
//        return messages.build();
//    }
}
