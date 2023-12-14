package com.devapi.responseObjects;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class BasicResponse<T> implements Response {
    String message;
    int status;
    long timestamp;
    T mainObject;
    List<T> mainObjects;

    public BasicResponse(String message, int status, List<T> mainObjects) {
        this(message, status, System.currentTimeMillis(), null, mainObjects);
    }

    public BasicResponse(String message, int status, T mainObject) {
        this(message, status, System.currentTimeMillis(), mainObject, null);
    }

    public BasicResponse(String message, int status) {
        this(message, status, System.currentTimeMillis(), null, null);
    }
}
