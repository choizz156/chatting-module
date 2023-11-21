package me.choizz.domainjpamodule.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    EXIST_EMAIL(400, "이미 가입한 e-mail입니다."),
    EXIST_NICKNAME(400,"이미 존재하는 닉네임입니다."),
    EXIST_CHATTING_ROOM(400, "이미 존재하는 채팅방입니다." );

    private final int code;
    private final String msg;
}
