package com.devapi.responseObjects;

import com.devapi.model.entities.User;

import java.util.List;

public class StandardUserResponse extends BasicResponse<User> {

    public StandardUserResponse(String message, int status, long timestamp, List<User> mainObjects) {
        super(message, status, timestamp, mainObjects);
    }

    public StandardUserResponse(String message, int status, long timestamp, User mainObject) {
        super(message, status, timestamp, mainObject);
    }

    public StandardUserResponse(String message, int status, long timestamp) {
        super(message, status, timestamp);
    }
}
