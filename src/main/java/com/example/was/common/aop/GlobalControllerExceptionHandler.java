package com.example.was.common.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleDefaultError() {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new CommonError("internal server error", "un handled error"));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleValidationError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new CommonError("validation error", "error when validate"));
    }

    private record CommonError(String error, String message) {
    }
}
