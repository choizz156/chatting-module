package me.choizz.websocketmodule.websocket.exception;

import com.mongodb.MongoClientException;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.exception.WebSocketBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "me.choizz.websocketmodule")
public class WebSocketExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");

    @ExceptionHandler(MongoClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void mongoTimeoutExceptionHandler(MongoTimeoutException e) {
        logger.warn("MongoClientException = {}", e.getMessage());
        ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(MongoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void mongoExceptionHandler(MongoException e) {
        logger.warn("mongodb error => {}", e.getMessage());
        ErrorResponse.of("알 수 없는 에러가 발생했습니다.");
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void authenticationServiceExceptionHandler(AuthenticationServiceException e) {
        logger.warn("authentication error => {}", e.getMessage());
        ErrorResponse.of(HttpStatus.UNAUTHORIZED, "인증이 되지 않은 요청입니다.");
    }

    @ExceptionHandler(MessageDeliveryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void messageDeliveryException(MessageDeliveryException e) {
        logger.warn("MessageDeliveryException error => {}", e.getMessage());
        ErrorResponse.of(HttpStatus.UNAUTHORIZED, "인증이 되지 않은 요청입니다.");
    }

    @ExceptionHandler(WebSocketBusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void webSocketBusinessExceptionHandler(WebSocketBusinessException e) {
        logger.warn("WebSocketBusinessException error -> {}", e.getMessage());
        ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
