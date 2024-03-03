package com.devapi.Exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomDevApiException extends Exception{

    private int statusCode;
    public CustomDevApiException(DevApiException devApiException){
        super(devApiException.getMessage());
        statusCode = devApiException.getCode();
    }

    public CustomDevApiException(String message){
        super(message);
        statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public CustomDevApiException(String message,int value){
        super(message);
        statusCode = value;
    }

}
