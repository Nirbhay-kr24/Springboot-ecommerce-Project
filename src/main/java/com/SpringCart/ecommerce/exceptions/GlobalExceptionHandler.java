package com.SpringCart.ecommerce.exceptions;

import com.SpringCart.ecommerce.exception.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.NOT_FOUND.value(),
                        "RESOURCE_NOT_FOUND",
                        ex.getMessage(),
                        request.getRequestURI()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    // 409 - Already Exists
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(
            AlreadyExistsException ex,
            HttpServletRequest request) {

        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.CONFLICT.value(),
                        "ALREADY_EXISTS",
                        ex.getMessage(),
                        request.getRequestURI()
                ),
                HttpStatus.CONFLICT
        );
    }

    // 400 - Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        "VALIDATION_FAILED",
                        message,
                        request.getRequestURI()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    // 400 - Illegal Arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_ARGUMENT",
                        ex.getMessage(),
                        request.getRequestURI()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    // 500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        // ✅ PROPER LOGGING
        log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "INTERNAL_SERVER_ERROR",
                        "Something went wrong. Please contact support.",
                        request.getRequestURI()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
