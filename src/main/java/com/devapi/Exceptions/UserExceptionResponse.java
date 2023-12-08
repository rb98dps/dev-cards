package com.devapi.Exceptions;

import com.devapi.responseObjects.Response;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserExceptionResponse implements Response {
    UserException userException;
    String errorMessage;
    String status;
    int statusCode;
    public UserExceptionResponse(String errorMessage, HttpStatus status) {
        this.status = status.name();
        this.statusCode = status.value();
        this.errorMessage = errorMessage;
    }

    public UserException getUserException() {
        return userException;
    }

    public void setUserException(UserException userException) {
        this.userException = userException;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UserExceptionResponse(UserException userException, HttpStatus status) {
        this.userException = userException;
        this.status = status.name();
        this.statusCode = status.value();

    }
}
