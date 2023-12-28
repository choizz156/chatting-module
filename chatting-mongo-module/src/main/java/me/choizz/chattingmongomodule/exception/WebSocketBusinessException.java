package me.choizz.chattingmongomodule.exception;

public class WebSocketBusinessException extends RuntimeException {

    public WebSocketBusinessException(final WebSocketExceptionCode webSocketExceptionCode) {
        super(webSocketExceptionCode.getMsg());
    }

    public WebSocketBusinessException(String msg){
        super(msg);
    }
}
