package com.juliusbaer.codility.error;

import com.juliusbaer.codility.api.domain.ApiError;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.http.HttpStatus;

import java.util.Map;

public enum ErrorCodes {
    PCO_1000(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occured. ", "An unknown error occured. Stacktrace: ${STACK_TRACE}"),
    PCO_1001(HttpStatus.NOT_FOUND, "User does not exists. ", "User Not Found with id: ${USER_ID}"),
    PCO_1002(HttpStatus.INTERNAL_SERVER_ERROR, "Database Operation Failed! ", "${ERROR}"),
    PCO_1003(HttpStatus.BAD_REQUEST, "PAO validation failed ", "Error during invoking the PAO endpoint /api/dfsk, Document id: {DOCUMENT_ID}"),
    PCO_1004(HttpStatus.BAD_REQUEST, "User already exists !", "User with ${EMAIL} already exists !")
    ;

    private static final String ERROR_CODE_PREFIX = "ERR_";
    private final HttpStatus httpStatus;
    private final String description;
    private final String errorMessage;

    ErrorCodes(final HttpStatus httpStatus, final String description, final String errorMessage) {
        this.httpStatus = httpStatus;
        this.description = description;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return ERROR_CODE_PREFIX.concat(this.name());
    }
    public String parseParams(final Map<String, Object> params) {
        if(params == null) {
            return errorMessage;
        }
        return StringSubstitutor.replace(errorMessage, params, "${", "}");
    }

    public String getLogEntry(final Map<String, Object> params) {
        return String.format("%s - %s", getErrorCode(), parseParams(params));
    }

    public String getDescription() {
        return description;
    }

    public ApiError asHttpResponse(Map<String, Object> params) {
        return ApiError.builder()
                .errorCode(this.getErrorCode())
                .message(this.getDescription())
                .error(this.parseParams(params))
                .params(params)
                .build();
    }
}
