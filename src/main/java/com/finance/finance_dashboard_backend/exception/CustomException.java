package com.finance.finance_dashboard_backend.exception;
// Custom exception class for handling application-specific errors
public class CustomException extends RuntimeException {
    // Constructor to pass error message
    public CustomException(String message) {
        super(message);
    }
}