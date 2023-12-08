package com.devapi.model.requestentities;

import com.devapi.model.entities.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Collection;
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
