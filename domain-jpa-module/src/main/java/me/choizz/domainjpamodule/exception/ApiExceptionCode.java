package me.choizz.domainjpamodule.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiExceptionCode {

    EXIST_EMAIL(400, "이미 가입한 e-mail입니다."),
    EXIST_NICKNAME(400, "이미 존재하는 닉네임입니다."),
    EXIST_CHATTING_ROOM(400, "이미 존재하는 채팅방입니다."),
    NOT_FOUND_UER(400, "찾을 수 없는 유저입니다."),
    NONE_AUTH(401, "로그인에 실패했습니다."),
    NONE_ACCESS(403,"권한이 없습니다." ),
    ALREADY_LOGIN(409, "이미 로그인돼있습니다.");

    private final int code;
    private final String msg;
}
