package com.devapi.controllers;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.dao.RoleRepository;
import com.devapi.services.RoleService;
import com.devapi.entities.Role;
import com.devapi.requestentities.CreateRoleRequest;
import com.devapi.requestentities.GetRoleRequest;
import com.devapi.responseObjects.BasicResponse;
import com.devapi.responseObjects.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RepositoryRestController(path = "/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/role")
    public Response getRole(@RequestBody GetRoleRequest getRoleRequest) throws CustomDevApiException {
        List<Role> roles = roleService.getRole(getRoleRequest);
        return new BasicResponse<>("success", HttpStatus.OK.value(), roles);
    }

    @PostMapping("/rolesList")
    public Response getRoles(@RequestBody List<GetRoleRequest> getRoleRequests) {
        List<Role> roles = roleService.getRoles(getRoleRequests);
        return new BasicResponse<>("success", HttpStatus.OK.value(), roles);
    }

    @PostMapping("/role")
    @ResponseBody
    public Response saveRole(@RequestBody CreateRoleRequest createRoleRequest) throws CustomDevApiException {
        Role savedRole = roleService.createRole(createRoleRequest);
        return new BasicResponse<>("success", HttpStatus.OK.value(), savedRole);
    }


}
