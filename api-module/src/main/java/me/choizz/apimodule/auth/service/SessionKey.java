package me.choizz.apimodule.auth.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SessionKey {
    LOGIN_USER("LOGIN_USER"),
    SPRING_SESSION_SESSIONS("spring:session:sessions:");

    private final String content;

    public static String of(final String sessionId){
        return SPRING_SESSION_SESSIONS.content + sessionId;
    }
}
