package com.tenjiku.omg.exception;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String message ) {
        super(message);
    }
}
