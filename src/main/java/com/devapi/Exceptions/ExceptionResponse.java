package com.devapi.Exceptions;

import com.devapi.responseObjects.Response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ExceptionResponse<T> implements Response {
    T exception;
    String errorMessage;
    String status;
    int statusCode;

    public ExceptionResponse(String errorMessage, HttpStatus status) {
        this.status = status.name();
        this.statusCode = status.value();
        this.errorMessage = errorMessage;
    }

    public ExceptionResponse(T exception, HttpStatus status) {
        this.exception = exception;
        this.status = status.name();
        this.statusCode = status.value();
    }
}
