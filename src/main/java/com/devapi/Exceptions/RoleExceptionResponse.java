package com.devapi.Exceptions;

import org.springframework.http.HttpStatus;

public class RoleExceptionResponse extends ExceptionResponse<RoleException> {

    public RoleExceptionResponse(String errorMessage, HttpStatus status) {
        super(errorMessage, status);
    }

    public RoleExceptionResponse(RoleException exception, HttpStatus status) {
        super(exception, status);
    }
}
