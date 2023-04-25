package com.juliusbaer.codility.error.handlers;

import com.juliusbaer.codility.api.domain.ApiError;
import com.juliusbaer.codility.error.ErrorCodes;
import com.juliusbaer.codility.error.exceptions.UnexpectedBusinessException;
import com.juliusbaer.codility.error.exceptions.UserNotFoundException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.util.Map;

@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    public final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ RuntimeException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public final ApiError handleUncaughtException(UserNotFoundException ex, WebRequest request) {
        return ErrorCodes.PCO_1000.asHttpResponse(Map.of("STACK_TRACE", ex.toString()));
    }

    @ExceptionHandler({ UserNotFoundException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public final ApiError handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, Object> params = Map.of("USER_ID", ex.getUserId());
        LOGGER.error(ErrorCodes.PCO_1001.getLogEntry(params));
        return ErrorCodes.PCO_1001.asHttpResponse(params);
    }

    @ExceptionHandler({ UnexpectedBusinessException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public final ApiError handleUnexpectedBusinessException(UnexpectedBusinessException ex) {
        return ex.getErrorCode().asHttpResponse(ex.getParams());
    }

    @ExceptionHandler({ SQLException.class, DataAccessException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public final ApiError databaseOperationFailedHandler(PSQLException ex) {
        return ErrorCodes.PCO_1002.asHttpResponse(
                Map.of("ERROR", ex.getMessage())
        );
    }
}