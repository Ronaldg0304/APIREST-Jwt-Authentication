package com.demo.autenticacion.autenticacionJWT.helpers;

import com.demo.autenticacion.autenticacionJWT.apiResponse.ApiError;
import com.demo.autenticacion.autenticacionJWT.apiResponse.ApiResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ApiResponseBuilder {

    public static <T> ApiResponse<T> success(T data, HttpStatus status) {
        return new ApiResponse<>( true, "Operation successful", data, status);
    }

    public static ApiResponse<Void> success(HttpStatus status) {
        return new ApiResponse<>(true, "Operation successful", null, status);
    }

    public static ApiResponse<Void> error(String type, String title, HttpStatus status, String errorMessage, String instance) {
        return new ApiResponse<>(
                false,
                type,
                title,
                status,
                instance,
                new ApiError(status, errorMessage )
        );
    }

    public static ApiResponse<Void> validationError(String type, String title, HttpStatus status, String errorMessage, String instance, Map<String, String> fieldErrors) {
        ApiError error = new ApiError(status, errorMessage);
        fieldErrors.forEach(error::withDetail);
        return new ApiResponse<>(false,
                type,
                title,
                status,
                instance,
                error);
    }
}
