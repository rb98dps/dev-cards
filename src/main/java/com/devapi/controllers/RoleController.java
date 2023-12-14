package com.devapi.controllers;

import com.devapi.Exceptions.RoleException;
import com.devapi.Exceptions.RoleExceptionResponse;
import com.devapi.dao.RoleRepository;
import com.devapi.model.entities.Role;
import com.devapi.model.requestentities.GetRoleRequest;
import com.devapi.responseObjects.Response;
import com.devapi.responseObjects.StandardRoleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RepositoryRestController(path = "/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/role")
    public ResponseEntity<Response> getRole(@RequestParam("id") UUID roleId, @RequestParam("name") String roleName) {
        List<Role> roles = null;
        if (null != roleId) {
            roles = roleRepository.findAllById(Collections.singletonList(roleId));
        } else if (null != roleName) {
            roles = roleRepository.findByRoleName(roleName);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RoleExceptionResponse("Required fields not found", HttpStatus.BAD_REQUEST));
        }
        return new ResponseEntity<>(new StandardRoleResponse("success", HttpStatus.OK.value(), roles), HttpStatus.OK);
    }

    @PostMapping("/rolesList")
    public ResponseEntity<Response> getRoles(@RequestBody List<GetRoleRequest> getRoleRequests) {
        List<Role> roles = new ArrayList<>();
        if (null != getRoleRequests) {
            roles.addAll(roleRepository.findAllById(getRoleRequests.stream().map(GetRoleRequest::getRoleId).distinct().collect(Collectors.toList())));
            roles.addAll(roleRepository.findAllByRoleIn(getRoleRequests.stream().map(GetRoleRequest::getRoleName).distinct().collect(Collectors.toList())));
            roles = removeDuplicates(roles);
        }
        return new ResponseEntity<>(new StandardRoleResponse("success", HttpStatus.OK.value(), roles), HttpStatus.OK);
    }

    @PostMapping("/role")
    @ResponseBody
    public ResponseEntity<Response> saveRole(@RequestBody Role role) {
        role.setRole(role.getRole().toUpperCase());
        Role savedRole;
        if (!roleRepository.existsByName(role.getRole())) {
            savedRole = roleRepository.save(role);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RoleExceptionResponse(RoleException.ROLE_ALREADY_EXIST, HttpStatus.BAD_REQUEST));
        }
        return new ResponseEntity<>(new StandardRoleResponse("success", HttpStatus.OK.value(), savedRole), HttpStatus.OK);
    }

    private <T> List<T> removeDuplicates(List<T> list) {
        // Convert the list to a Set (which automatically removes duplicates)
        HashSet<T> set = new HashSet<>();
        List<T> list1 = new ArrayList<>();
        for (T tuple : list) {
            if (!set.contains(tuple)) {
                set.add(tuple);
                list1.add(tuple);
            }
        }
        return list1;
    }
}
