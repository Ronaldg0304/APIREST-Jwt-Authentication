package com.demo.autenticacion.autenticacionJWT.exceptions;

import com.demo.autenticacion.autenticacionJWT.apiResponse.ApiResponse;
import com.demo.autenticacion.autenticacionJWT.helpers.ApiResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Recurso no encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseBuilder.error(
                        "RESOURCE_NOT_FOUND",
                        "Recurso no encontrado",
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    // 2. Credenciales inválidas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseBuilder.error(
                        "BAD_CREDENTIALS",
                        "Usuario o contraseña incorrecta",
                        HttpStatus.UNAUTHORIZED,
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    // 3. Validaciones con @Valid en DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Invalid value"),
                        (msg1, msg2) -> msg1 + "; " + msg2
                ));

        return ResponseEntity.badRequest()
                .body(ApiResponseBuilder.validationError(
                        "VALIDATION_ERROR",
                        "Error de validación",
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        request.getRequestURI(),
                        errors));
    }

    // 4. Validaciones con @Validated en parámetros
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

        return ResponseEntity.badRequest()
                .body(ApiResponseBuilder.validationError(
                        "CONSTRAINT_ERROR",
                        "Error de restricción",
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        request.getRequestURI(),
                        errors));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        if (message.contains("users.email")) {
            errors.put("email", "Email is already in use");
        } else if (message.contains("users.username")) {
            errors.put("username", "Username is already taken");
        } else {
            errors.put("constraint", "Unique constraint violation");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseBuilder.validationError("CONSTRAINT CONFLICT",
                "Conflicto por restricción",
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI(),
                errors));
    }
    // 5. Parámetros faltantes
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request) {
        Map<String, String> errors = Map.of(ex.getParameterName(), "is required");

        return ResponseEntity.badRequest()
                .body(ApiResponseBuilder.validationError(
                        "MISSING_PARAM",
                        "Parámetro faltante",
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        request.getRequestURI(),
                        errors));
    }

    // 6. Error de tipo de argumento
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        Map<String, String> errors = Map.of(ex.getName(), "Debe ser de tipo " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName());

        return ResponseEntity.badRequest()
                .body(ApiResponseBuilder.validationError(
                        "TYPE_MISMATCH",
                        "Error de tipo de argumento",
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        request.getRequestURI(),
                        errors));
    }

    // 7. JSON malformado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleMalformedJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Map<String, String> errors = Map.of("json", ex.getMostSpecificCause().getMessage());

        return ResponseEntity.badRequest()
                .body(ApiResponseBuilder.validationError(
                        "MALFORMED_JSON",
                        "JSON malformado",
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        request.getRequestURI(),
                        errors));
    }

    // 8. Método no soportado
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        Map<String, String> errors = Map.of("method", ex.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponseBuilder.validationError(
                        "METHOD_NOT_ALLOWED",
                        "Método no soportado",
                        HttpStatus.METHOD_NOT_ALLOWED,
                        ex.getMessage(),
                        request.getRequestURI(),
                        errors));
    }

    // 9. Media type no soportado
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        Map<String, String> errors = Map.of("mediaType", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(ApiResponseBuilder.validationError(
                        "UNSUPPORTED_MEDIA_TYPE",
                        "Media type no soportado",
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        ex.getMessage(),
                        request.getRequestURI(),
                        errors));
    }

    // 10. Fallback genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        Map<String, String> errors = Map.of("error", ex.getMessage());

        return ResponseEntity.internalServerError()
                .body(ApiResponseBuilder.validationError(
                        "INTERNAL_SERVER_ERROR",
                        "Error interno del servidor",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getMessage(),
                        request.getRequestURI(),
                        errors));
    }
}
