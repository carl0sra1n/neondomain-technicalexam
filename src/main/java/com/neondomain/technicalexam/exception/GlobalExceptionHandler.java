package com.neondomain.technicalexam.exception;

import org.hibernate.exception.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.neondomain.technicalexam.model.ErrorResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<String> errorMessages = result.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse("Validation failed", errorMessages);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errorMessages = fieldErrors.stream()
                .map(this::buildErrorMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse("Binding error", errorMessages);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Throwable cause = ex.getCause();
        String errorMessage;
    
        if (cause instanceof SQLException) {
            SQLException sqlException = (SQLException) cause;
    
            if (isDuplicateKeyViolation(sqlException)) {
                errorMessage = "Duplicate registration detected: Try with a different user name.\n\n" + sqlException.getMessage();
            } else {
                errorMessage = "Database constraint violation: " + sqlException.getMessage();
            }
        } else {
            errorMessage = "Constraint violation: " + ex.getMessage();
        }
    
        ErrorResponse errorResponse = new ErrorResponse("Constraint violation", Arrays.asList(errorMessage));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    private boolean isDuplicateKeyViolation(SQLException sqlException) {
        return "23505".equals(sqlException.getSQLState()) || sqlException.getErrorCode() == 1062;
    }

    private String buildErrorMessage(FieldError fieldError) {
        if ("age".equals(fieldError.getField()) && !isNumeric(fieldError.getRejectedValue())) {
            return "Invalid value for age. Please provide a valid number.";
        } else {
            return fieldError.getField() + ": " + fieldError.getDefaultMessage();
        }
    }

    private boolean isNumeric(Object value) {
        if (value == null) {
            return false;
        }
        try {
            Long.parseLong(value.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}