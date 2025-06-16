package com.tenjiku.omg.exception;

public class PriceAlreadyExistException extends RuntimeException {
    public PriceAlreadyExistException(String message) {
        super(message);
    }
}
