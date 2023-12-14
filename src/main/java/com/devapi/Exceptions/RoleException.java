package com.devapi.Exceptions;

import org.springframework.http.HttpStatus;

public enum RoleException {
    ROLE_NOT_FOUND("Role does not exist",HttpStatus.NOT_FOUND),
    ROLE_ALREADY_EXIST("Role already present",HttpStatus.FOUND);

    RoleException(String message, HttpStatus statusCode) {
        int statusCode1 = statusCode.value();
        String status = statusCode.name();
    }
}
