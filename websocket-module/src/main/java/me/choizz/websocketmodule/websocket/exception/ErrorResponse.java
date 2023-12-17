//package me.choizz.websocketmodule.websocket.exception;
//
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonInclude.Include;
//import lombok.Builder;
//import lombok.Getter;
//import org.springframework.http.HttpStatus;
//
//@Getter
//@JsonInclude(Include.NON_EMPTY)
//public class ErrorResponse {
//
//    private final int status;
//    private final String msg;
//
//    @Builder
//    private ErrorResponse(int status, String msg) {
//        this.status = status;
//        this.msg = msg;
//    }
//
//    public static ErrorResponse of(HttpStatus httpStatus, String msg) {
//        return builder()
//            .status(httpStatus.value())
//            .msg(msg)
//            .build();
//    }
//
//    public static ErrorResponse of(String msg) {
//        return builder()
//            .status(HttpStatus.BAD_REQUEST.value())
//            .msg(msg)
//            .build();
//    }
//
//}
//
