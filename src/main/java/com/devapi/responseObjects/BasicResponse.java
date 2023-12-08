package com.devapi.responseObjects;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BasicResponse<T> implements Response {
    String message;
    int status;
    long timestamp;
    T mainObject;
    List<T> mainObjects;

    public BasicResponse(String message, int status, long timestamp, List<T> mainObjects) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.mainObjects = mainObjects;
    }

    public BasicResponse(String message, int status, long timestamp, T mainObject) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.mainObject = mainObject;
    }

    public BasicResponse(String message, int status, long timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public T getMainObject() {
        return mainObject;
    }

    public void setMainObject(T mainObject) {
        this.mainObject = mainObject;
    }

    public List<T> getMainObjects() {
        return mainObjects;
    }

    public void setMainObjects(List<T> mainObjects) {
        this.mainObjects = mainObjects;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
