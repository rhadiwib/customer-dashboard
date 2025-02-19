package com.regional.customerdashboard.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ManagerNotFoundException.class)
    public ResponseEntity<Object> handleManagerNotFound(ManagerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    public static class ManagerNotFoundException extends RuntimeException {
        public ManagerNotFoundException(Long managerId) {
            super("Manager with id " + managerId + " not found");
        }
    }
}