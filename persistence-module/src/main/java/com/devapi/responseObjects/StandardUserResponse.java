package com.devapi.responseObjects;

import com.devapi.entities.User;

import java.util.List;

public class StandardUserResponse extends BasicResponse<User> {

    public StandardUserResponse(String message, int status, long timestamp, User mainObject, List<User> mainObjects) {
        super(message, status, timestamp, mainObject, mainObjects);
    }

    public StandardUserResponse(String message, int status, List<User> mainObjects) {
        super(message, status, mainObjects);
    }

    public StandardUserResponse(String message, int status, User mainObject) {
        super(message, status, mainObject);
    }

    public StandardUserResponse(String message, int status) {
        super(message, status);
    }
}
