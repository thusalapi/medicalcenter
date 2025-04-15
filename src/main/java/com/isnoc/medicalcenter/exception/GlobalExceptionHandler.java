package com.isnoc.medicalcenter.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List; // Import List
import java.util.Map;
import java.util.stream.Collectors; // Import Collectors

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Specific handler for ResourceNotFoundException (No changes)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        // ... (implementation as before) ...
        log.warn("Resource not found: {}", ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Handler for IllegalArgumentException (No changes)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        // ... (implementation as before) ...
        log.warn("Bad request: {}", ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Handler for Validation Errors (Verified)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.warn("Validation failed: {}", ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");

        // Get all validation errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                // Use getField() + " " + getDefaultMessage() for more context
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("messages", errors); // List of validation messages
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    // Generic handler for other unhandled exceptions (No changes)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(
            Exception ex, WebRequest request) {
        // ... (implementation as before) ...
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred. Please contact support."); // User-friendly message
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}