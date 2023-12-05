package me.choizz.domainjpamodule.exception;

public class ApiBusinessLogicException extends RuntimeException {

    public ApiBusinessLogicException(final ApiExceptionCode apiExceptionCode) {
        super(apiExceptionCode.getMsg());
    }

    public ApiBusinessLogicException(String msg){
        super(msg);
    }
}
