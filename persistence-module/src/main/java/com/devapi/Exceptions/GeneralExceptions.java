package com.devapi.Exceptions;

import org.springframework.http.HttpStatus;

public enum GeneralExceptions implements DevApiException{
    BadRequestException(HttpStatus.BAD_REQUEST.value(), "Bad Request Found");

    final int value;
    final String message;
    GeneralExceptions(int value, String s) {
        this.value = value;
        this.message = s;
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
