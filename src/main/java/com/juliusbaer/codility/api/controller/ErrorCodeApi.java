
package com.juliusbaer.codility.api.controller;

import com.juliusbaer.codility.api.domain.ApiErrorCode;
import com.juliusbaer.codility.error.ErrorCodes;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@NoArgsConstructor
public class ErrorCodeApi {
    @GetMapping(value={"/api/error-codes"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApiErrorCode>> getErrorCodes() {
        List<ApiErrorCode> response = new ArrayList<>();

        Arrays.stream(ErrorCodes.values()).forEachOrdered(x -> {
            ApiErrorCode err = ApiErrorCode.builder()
                    .errorCode(x.getErrorCode())
                    .description(x.getDescription())
                    .loggingPattern(x.getLogEntry(null)).build();
            response.add(err);
        });
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping(value={"/error-codes-plain"})
    public ResponseEntity<String> healthCheck() {
        String response = "<html><head><title>PCO - Error Codes</title></head><body><table><th>Error Code</th><th>Description</th><th>Logging Pattern</th>";

        for( ErrorCodes x : ErrorCodes.values()) {
            ApiErrorCode err = ApiErrorCode.builder().errorCode(x.getErrorCode()).description(x.getDescription()).loggingPattern(x.getLogEntry(null)).build();
            response = response.concat(err.asHtml());
        }
        response = response.concat("</table></body></html>");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/error-codes")
    public String errorCodes(Model model) {
        ResponseEntity<List<ApiErrorCode>> response = getErrorCodes();
        model.addAttribute("errors", response.getBody());
        return "error-codes";
    }
}
