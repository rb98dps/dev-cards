package com.devapi.Exceptions;

import org.springframework.http.HttpStatus;

public enum RoleException implements DevApiException{
    ROLE_NOT_FOUND("Role does not exist",HttpStatus.NOT_FOUND),
    ROLE_ALREADY_EXIST("Role already present",HttpStatus.FOUND);

    final String message;
    final int value;

    RoleException(String message, HttpStatus statusCode) {
        this.message = message;
        this.value = statusCode.value();
    }
    @Override
    public int getCode() {
        return value;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
