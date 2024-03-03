package com.devapi.requestentities;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;
@Data
public class CreateUserRequest {

    private UUID id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String password;

    @NonNull
    private String email;
}
