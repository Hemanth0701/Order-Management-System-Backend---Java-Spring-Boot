package com.tenjiku.omg.exception;

public class ProductAlreadyDeletedException extends RuntimeException {
    public ProductAlreadyDeletedException(String message) {
        super(message);
    }
}
