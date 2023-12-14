package com.devapi.Exceptions;


import org.springframework.http.HttpStatus;

public class UserExceptionResponse extends ExceptionResponse<UserException> {

    public UserExceptionResponse(String errorMessage, HttpStatus status) {
        super(errorMessage, status);
    }

    public UserExceptionResponse(UserException exception, HttpStatus status) {
        super(exception, status);
    }
}
