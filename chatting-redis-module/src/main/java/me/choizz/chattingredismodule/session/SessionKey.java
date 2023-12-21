package me.choizz.chattingredismodule.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SessionKey {
    LOGIN_USER("LOGIN_USER"),
    SPRING_SESSION_SESSIONS("spring:session:sessions:");

    private final String content;

    public static String of(final String sessionId){
        return SPRING_SESSION_SESSIONS.content + sessionId;
    }
}
