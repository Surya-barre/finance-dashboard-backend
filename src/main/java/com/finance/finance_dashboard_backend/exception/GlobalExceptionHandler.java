package com.finance.finance_dashboard_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
// Handles all exceptions globally
public class GlobalExceptionHandler {

    // Handle custom exceptions (access denied, inactive user, etc.)
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(CustomException ex) {

        HttpStatus status;

        String message = ex.getMessage().toLowerCase();

        if (message.contains("access")) {
            status = HttpStatus.FORBIDDEN; // 403
        } else if (message.contains("not found")) {
            status = HttpStatus.NOT_FOUND; // 404
        } else if (message.contains("inactive")) {
            status = HttpStatus.BAD_REQUEST; // 400
        } else {
            status = HttpStatus.BAD_REQUEST; // default
        }

        return ResponseEntity
                .status(status)
                .body(Map.of("error", ex.getMessage()));
    }

    // Handle validation errors (@Valid)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    // Handle JSON / enum parsing errors
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleEnumError(
            org.springframework.http.converter.HttpMessageNotReadableException ex) {

        String message = ex.getMostSpecificCause().getMessage();

        if (message.contains("RecordType")) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid type. Use INCOME or EXPENSE"));
        }

        if (message.contains("Role")) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid role. Use ADMIN, ANALYST, VIEWER"));
        }

        return ResponseEntity
                .badRequest()
                .body(Map.of("error", "Invalid request format"));
    }

    // Handle all other unexpected errors (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Something went wrong"));
    }
}