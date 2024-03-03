package com.devapi.responseObjects;

import com.devapi.entities.Role;

import java.util.List;

public class StandardRoleResponse extends BasicResponse<Role>{


    public StandardRoleResponse(String message, int status, long timestamp, Role mainObject, List<Role> mainObjects) {
        super(message, status, timestamp, mainObject, mainObjects);
    }

    public StandardRoleResponse(String message, int status, List<Role> mainObjects) {
        super(message, status, mainObjects);
    }

    public StandardRoleResponse(String message, int status, Role mainObject) {
        super(message, status, mainObject);
    }

    public StandardRoleResponse(String message, int status) {
        super(message, status);
    }
}
