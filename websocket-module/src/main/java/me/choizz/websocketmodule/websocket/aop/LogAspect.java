package me.choizz.websocketmodule.websocket.aop;

import lombok.extern.slf4j.Slf4j;
import me.choizz.websocketmodule.websocket.exception.ResponseDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component("webSocketLogAspect")
public class LogAspect {

    private static final String TRACE_ID = "TRACE_ID";
    private static final String TARGET = "TARGET";
    private static final Logger logger = LoggerFactory.getLogger("fileLog");

    @Pointcut("execution(* me.choizz.websocketmodule.websocket.message..*(..))")
    public void controllerLog() {
    }

    @Pointcut("execution(* me.choizz.websocketmodule.websocket.message..sendChat(..))")
    public void message() {
    }

    @Pointcut("execution(* me.choizz.websocketmodule.websocket..*Handler(..))")
    public void exceptionLog() {
    }

    @Before("message()")
    public void message(JoinPoint joinpoint) {
        MDC.put(TRACE_ID, (String) joinpoint.getArgs()[0]);
        MDC.put(TARGET, joinpoint.getSignature().getDeclaringType().getSimpleName());
        logger.info("request => {}", joinpoint.getSignature().getName());
    }

    @AfterReturning(value = "message()", returning = "result")
    public void message(JoinPoint joinpoint, ResponseDto<?> result) {
        logger.info("response::: {} result = {}", joinpoint.getSignature().getName(), result.time());
        MDC.clear();
    }

    @Before("controllerLog() && !message()")
    public void requestLogging(JoinPoint joinpoint) {
        MDC.put(TARGET, joinpoint.getSignature().getDeclaringType().getSimpleName());
        logger.info("request => {}", joinpoint.getSignature().getName());
    }

    @AfterReturning(value = "controllerLog() && !message()", returning = "result")
    public void requestLogging(JoinPoint joinpoint, Object result) {
        logger.info("response::: {} result = {}", joinpoint.getSignature().getName(), result);
    }

    @AfterReturning(value = "exceptionLog()", returning = "result")
    public void exceptionLog(JoinPoint joinpoint, Object result) {
        logger.info("websocket exception => {}, result = {}",
            joinpoint.getSignature(),
            result
        );
    }
}
