package me.choizz.apimodule.aop;

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
@Component
public class LogAspect {

    private static final String TARGET = "TARGET";
    private final Logger logger = LoggerFactory.getLogger("fileLog");

    @Pointcut("execution(* me.choizz.apimodule.api.controller..*Controller.*(..))")
    public void controllerLog() {
    }

    @Pointcut("execution(* me.choizz.apimodule.api.controller..*Handler.*(..))")
    public void exceptionLog() {
    }

    @AfterReturning(value = "exceptionLog()", returning = "result")
    public void exceptionLog(JoinPoint joinpoint, Object result) {
        logger.info("exception result => {}", result);
    }

    @Before("controllerLog()")
    public void requestLogging(JoinPoint joinpoint) {
        MDC.put(TARGET, joinpoint.getSignature().getDeclaringType().getSimpleName());
        logger.info("request => {}", joinpoint.getSignature().getName());
    }

    @AfterReturning(value = "controllerLog()", returning = "result")
    public void requestLogging(JoinPoint joinpoint, Object result) {
        logger.info("{} response::: {}", joinpoint.getSignature().getName(), result);
    }
}
