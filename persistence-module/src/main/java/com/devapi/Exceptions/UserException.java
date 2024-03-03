package com.devapi.Exceptions;

import org.springframework.http.HttpStatus;

public enum UserException implements DevApiException{

    USER_NOT_FOUND("User does not exist", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXIST("User already present",HttpStatus.FOUND);

    final String message;
    final int value;

    UserException(String message, HttpStatus statusCode) {
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
