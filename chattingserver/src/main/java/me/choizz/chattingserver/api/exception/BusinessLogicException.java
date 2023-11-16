package me.choizz.chattingserver.api.exception;

public class BusinessLogicException extends RuntimeException {

    public BusinessLogicException(final ExceptionCode exceptionCode) {
        super(exceptionCode.getMsg());
    }

    public BusinessLogicException(String msg){
        super(msg);
    }
}
