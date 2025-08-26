package com.demo.autenticacion.autenticacionJWT.apiResponse;

import lombok.Getter;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class ApiResponse<T> {
    private boolean success;
    private String type;
    private String title;
    private HttpStatus status;
    private T data;
    private String timestamp;
    private String instance;
    private ApiError error;  // Only populated when success=false


    public ApiResponse(boolean success,String title, T data, HttpStatus status) {
        this.success = success;
        this.title = title;
        this.data = data;
        this.status = status;
        this.timestamp = Instant.now().toString();

    }

    public ApiResponse(boolean success, String type, String title, HttpStatus status, String instance, ApiError error) {
        this.success = success;
        this.type = type;
        this.title = title;
        this.status = status;
        this.timestamp = Instant.now().toString();
        this.instance = instance;
        this.error = error;
    }}

