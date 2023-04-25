package com.juliusbaer.codility.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juliusbaer.codility.error.ErrorCodes;
import com.juliusbaer.codility.error.exceptions.UnexpectedBusinessException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggerAspect {

    Logger log = LoggerFactory.getLogger(LoggerAspect.class);
    private final ObjectMapper mapper = new ObjectMapper();

    public LoggerAspect() throws UnknownHostException {
    }

    @Around("execution(public * *(..)) && within(com.juliusbaer.codility..*)")
    private Object logAroundEveryPublicMethod(ProceedingJoinPoint pjp) throws Throwable {
        return logInOutParams(pjp);
    }

    private Object logInOutParams(ProceedingJoinPoint pjp) throws Throwable {
//        String methodName = pjp.getSignature().getName();
//        String className = pjp.getTarget().getClass().toString();
//        int linenNumber = pjp.getSourceLocation().getLine();

        Object[] args = pjp.getArgs();
        List<String> paramsAsListOfString = Arrays.stream(args).map(x -> {
            try {
                return mapper.writeValueAsString(x);
            } catch (JsonProcessingException e) {
                return x.toString();
            }
        }).collect(Collectors.toList());
        log.info("Entering {} parameters: {}", pjp.getSignature(), paramsAsListOfString);
        Object returnedVal = pjp.proceed();
        log.info("Exiting {} result: {}", pjp.getSignature(), returnedVal);
        return returnedVal;
    }

    @AfterThrowing(pointcut = "execution(public * *(..)) && within(com.juliusbaer.codility..*)", throwing = "e")
    public void afterThrowing(Exception e) throws Throwable {
        if(e instanceof SQLException ||  e instanceof DataAccessException) {
            log.error("{}", ErrorCodes.PCO_1002.getLogEntry(Map.of("ERROR", e.getMessage())) , e);
            return;
        }
        if(e instanceof UnexpectedBusinessException) {
            log.error("{}", ((UnexpectedBusinessException) e).getErrorCode().getLogEntry(((UnexpectedBusinessException) e).getParams()) , e);
            return;
        }
        if(e instanceof RuntimeException) {
            log.error("{}", ErrorCodes.PCO_1000.getLogEntry(
                    Map.of("STACK_TRACE", ExceptionUtils.getStackTrace(e))
            ), e);
        }
    }
}