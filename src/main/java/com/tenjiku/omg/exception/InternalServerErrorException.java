package com.tenjiku.omg.exception;

public class InternalServerErrorException extends RuntimeException {
  public InternalServerErrorException(String message) {
    super(message);
  }
}
