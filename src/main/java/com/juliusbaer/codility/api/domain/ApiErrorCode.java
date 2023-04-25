package com.juliusbaer.codility.api.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorCode {

    private String errorCode;
    private String description;
    private String loggingPattern;

    public String asHtml() {
        return "<tr>" +
                "<td>" + errorCode + "</td>" +
                "<td>" + description + "</td>" +
                "<td>" + loggingPattern + "</td>" +
                "</tr>";
    }
}
