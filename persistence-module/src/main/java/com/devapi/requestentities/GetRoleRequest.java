package com.devapi.requestentities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GetRoleRequest {
    UUID roleId;
    String roleName;
}
