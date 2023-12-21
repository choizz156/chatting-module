package me.choizz.websocketmodule.websocket.exception;

import com.mongodb.MongoClientException;
import com.mongodb.MongoTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WebSocketExceptionHandler {

    @ExceptionHandler(MongoClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void mongoTimeoutExceptionHandler(MongoTimeoutException e) {
        log.error("MongoClientException = {}", e.getMessage());
        ErrorResponse.of(e.getMessage());
    }
}
