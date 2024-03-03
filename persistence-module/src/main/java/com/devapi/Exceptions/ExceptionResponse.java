package com.devapi.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.devapi.responseObjects.Response;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ExceptionResponse implements Response {
    String errorMessage;
    String status;
    int statusCode;

    public ExceptionResponse(String errorMessage, HttpStatus status) {
        this.status = status.name();
        this.statusCode = status.value();
        this.errorMessage = errorMessage;
    }

    public ExceptionResponse(CustomDevApiException exception) {
        errorMessage = exception.getMessage();
        HttpStatus httpStatus = HttpStatus.resolve(exception.getStatusCode());
        if(httpStatus!=null)
        {
            this.status = httpStatus.getReasonPhrase();
        }
        this.statusCode = exception.getStatusCode();
    }

    public ExceptionResponse(Exception exception) {
        errorMessage = exception.getMessage();
        status = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
