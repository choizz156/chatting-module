package me.choizz.websocketmodule.websocket.aop;

import lombok.extern.slf4j.Slf4j;
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
    private final Logger logger = LoggerFactory.getLogger("fileLog");

    @Pointcut("execution(* me.choizz.websocketmodule.websocket.message..*(..))")
    public void controllerLog() {
    }

    @Pointcut("execution(* me.choizz.websocketmodule.websocket.message..sendChat(..))")
    public void messageLog() {
    }

    @Pointcut("execution(* me.choizz.websocketmodule.websocket..*Handler(..))")
    public void exceptionLog() {
    }

    @Before("messageLog()")
    public void messageLog(JoinPoint joinpoint) {
        MDC.put(TRACE_ID, (String) joinpoint.getArgs()[0]);
        MDC.put(TARGET, joinpoint.getSignature().getDeclaringType().getSimpleName());
        logger.info("request => {}", joinpoint.getSignature().getName());
    }

    @AfterReturning(value = "messageLog()", returning = "result")
    public void messageLog(JoinPoint joinpoint, Object result) {
        logger.info("response::: {} ", joinpoint.getSignature().getName());
        MDC.clear();
    }

    @Before("controllerLog() && !messageLog()")
    public void requestLogging(JoinPoint joinpoint) {
        MDC.put(TARGET, joinpoint.getSignature().getDeclaringType().getSimpleName());
        logger.info("request => {}", joinpoint.getSignature().getName());
    }

    @AfterReturning(value = "controllerLog() && !messageLog()", returning = "result")
    public void requestLogging(JoinPoint joinpoint, Object result) {
        logger.info("response::: {} ", joinpoint.getSignature().getName());
    }

    @AfterReturning(value = "exceptionLog()", returning = "result")
    public void exceptionLog(JoinPoint joinpoint, Object result) {
        logger.info("websocket exception => {}, result = {}",
            joinpoint.getSignature(),
            result
        );
    }
}
