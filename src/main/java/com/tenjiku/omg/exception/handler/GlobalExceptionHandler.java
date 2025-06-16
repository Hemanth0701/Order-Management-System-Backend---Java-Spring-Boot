package com.tenjiku.omg.exception.handler;

import com.tenjiku.omg.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ────── Common Framework Exceptions ────── //

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors.toString(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String expectedType = (ex.getRequiredType() != null) ? ex.getRequiredType().getSimpleName() : "unknown";
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(), ex.getName(), expectedType);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Type mismatch", message, request);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), request);
    }

    // ────── Custom Domain Exceptions ────── //

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UserAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Username already exists", ex.getMessage(), request);
    }


    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPassword(InvalidPasswordException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid password", ex.getMessage(), request);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalError(InternalServerErrorException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex.getMessage(), request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "User not found", ex.getMessage(), request);
    }

    @ExceptionHandler(UserAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyDeleted(UserAlreadyDeletedException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "User already deleted", ex.getMessage(), request);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Insufficient stock", ex.getMessage(), request);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Order not found", ex.getMessage(), request);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ErrorResponse> handlePermissionDenied(PermissionDeniedException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Permission denied", ex.getMessage(), request);
    }

    @ExceptionHandler(PriceAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handlePriceAlreadyExists(PriceAlreadyExistException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Price already exists", ex.getMessage(), request);
    }

    @ExceptionHandler(ProductAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyDeleted(ProductAlreadyDeletedException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.GONE, "Product already deleted", ex.getMessage(), request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Product not found", ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), request);
    }

    // ────── Catch-All Handler ────── //

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Runtime exception", ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", ex.getMessage(), request);
    }

    // ────── Response Builder ────── //

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error, String message, WebRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(response, status);
    }
}