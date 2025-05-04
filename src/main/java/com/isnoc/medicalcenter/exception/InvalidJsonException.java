package com.isnoc.medicalcenter.exception;

/**
 * Exception thrown when JSON operations fail
 */
public class InvalidJsonException extends RuntimeException {

    public InvalidJsonException(String message) {
        super(message);
    }

    public InvalidJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}