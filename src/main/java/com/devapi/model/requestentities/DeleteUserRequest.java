package com.devapi.model.requestentities;

import lombok.Data;

import java.util.UUID;
@Data
public class DeleteUserRequest {
    String email;
    UUID id;
}
