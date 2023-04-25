package com.juliusbaer.codility.error.exceptions;

import com.juliusbaer.codility.error.ErrorCodes;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Data
public class UnexpectedBusinessException extends RuntimeException {
    private final ErrorCodes errorCode;

    private final Map<String, Object> params;
}
