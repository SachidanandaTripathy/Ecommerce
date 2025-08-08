package com.blibli.Customer.exception;

import com.blibli.Customer.dto.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<GenericResponse<String>> handleCustomException(CustomException ex) {
        GenericResponse<String> response = new GenericResponse<>(
                false,
                ex.getMessage(),
                null,null
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<String>> handleGenericException(Exception ex) {
        GenericResponse<String> response = new GenericResponse<>(
                false,
                "Internal server error: " + ex.getMessage(),
                null,null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
