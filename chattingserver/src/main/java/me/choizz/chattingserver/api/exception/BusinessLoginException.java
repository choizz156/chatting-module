package me.choizz.chattingserver.api.exception;

public class BusinessLoginException extends RuntimeException {

    public BusinessLoginException(final ExceptionCode exceptionCode) {
        super(exceptionCode.getMsg());
    }

    public BusinessLoginException(String msg){
        super(msg);
    }
}
