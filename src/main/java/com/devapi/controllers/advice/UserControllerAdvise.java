package com.devapi.controllers.advice;

import com.devapi.Exceptions.UserExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class UserControllerAdvise {
    @ExceptionHandler
    public ResponseEntity<UserExceptionResponse> handleException(Exception exception) {
        return new ResponseEntity<>( new UserExceptionResponse(exception.getMessage()
                ,HttpStatus.INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
