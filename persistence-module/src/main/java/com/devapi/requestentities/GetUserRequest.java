package com.devapi.requestentities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class GetUserRequest {
    private UUID id;
    private String email;


}
