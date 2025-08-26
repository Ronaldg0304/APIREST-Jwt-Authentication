package com.demo.autenticacion.autenticacionJWT.apiResponse;

import lombok.Data;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiError {
    private HttpStatus status;        // Machine-readable error code
    private String description; // Human-readable description
    private String traceId;     // For distributed tracing
    private Map<String, String> details; // Additional error context


    public ApiError(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
        this.traceId = MDC.get("traceId"); // From SLF4J MDC
    }

    public ApiError withDetail(String key, String value) {
        if (this.details == null) {
            this.details = new HashMap<>();
        }
        this.details.put(key, value);
        return this;
    }
}
