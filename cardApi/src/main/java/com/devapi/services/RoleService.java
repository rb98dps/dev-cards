package com.devapi.services;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.entities.Role;
import com.devapi.requestentities.CreateRoleRequest;
import com.devapi.requestentities.GetRoleRequest;

import java.util.List;

public interface RoleService {

    public List<Role> getDefaultRole() throws CustomDevApiException;
    public List<Role> getRole(GetRoleRequest getRoleRequest) throws CustomDevApiException;

    public Role createRole(CreateRoleRequest createRoleRequest) throws CustomDevApiException;

    public List<Role> getRoles( List<GetRoleRequest> getRoleRequests);
}
