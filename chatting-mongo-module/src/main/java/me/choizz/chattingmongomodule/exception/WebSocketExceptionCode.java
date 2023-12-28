package me.choizz.chattingmongomodule.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebSocketExceptionCode {

   NO_CHAT_ROOM(400,"존재하지 않는 채팅룸입니다.");

    private final int code;
    private final String msg;
}
