package com.devapi.Exceptions;

import com.devapi.responseObjects.Response;
import org.springframework.http.HttpStatus;

public enum UserException{

    USER_NOT_FOUND("User does not exist", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXIST("User already present",HttpStatus.FOUND);

    UserException(String message, HttpStatus statusCode) {
        int statusCode1 = statusCode.value();
        String status = statusCode.name();
    }
}
