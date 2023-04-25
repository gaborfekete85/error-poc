package com.juliusbaer.codility.api.domain;

import com.juliusbaer.codility.error.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
@Builder
@Data
@AllArgsConstructor
public class ApiError {
    private String errorCode;
    private String message;
    private String error;
    private Map<String, Object> params;

    public static ApiError of(ErrorCodes errorCode, Map<String, Object> params) {
        return new ApiError(errorCode.getErrorCode(), errorCode.getDescription(), errorCode.parseParams(params), params);
    }
}

