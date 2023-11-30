package me.choizz.domainjpamodule.exception;

public class ApiBusinessLogicException extends RuntimeException {

    public ApiBusinessLogicException(final ExceptionCode exceptionCode) {
        super(exceptionCode.getMsg());
    }

    public ApiBusinessLogicException(String msg){
        super(msg);
    }
}
