package com.devapi.requestentities;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AssignRoleRequest {
    String userEmail;
    UUID userId;
    List<GetRoleRequest> assignRoles;
}
