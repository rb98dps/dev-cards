package com.devapi.controllers.advice;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.Exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class UserControllerAdvise {
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        if(exception instanceof CustomDevApiException customDevApiException){
            HttpStatus httpStatus = HttpStatus.resolve(customDevApiException.getStatusCode());

            return new ResponseEntity<>(new ExceptionResponse(customDevApiException),httpStatus!=null?httpStatus:HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(new ExceptionResponse(exception),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
