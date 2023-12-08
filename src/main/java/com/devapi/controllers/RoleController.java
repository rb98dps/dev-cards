package com.devapi.controllers;

import com.devapi.dao.RoleRepository;
import com.devapi.model.entities.Role;
import com.devapi.model.requestentities.GetRoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RepositoryRestController(path = "/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/role")
    @ResponseBody
    public List<Role> getRole(@RequestBody GetRoleRequest getRoleRequest) {
        if(null!=getRoleRequest.getRoleId()){
            return roleRepository.findAllById(Collections.singletonList(getRoleRequest.getRoleId()));
        }
        else{
            return roleRepository.findByRoleName(getRoleRequest.getRoleName());
        }
    }

    @GetMapping("/rolesList")
    @ResponseBody
    public List<Role> getRoles(@RequestBody List<GetRoleRequest> getRoleRequests) {
        List<Role> roles = new ArrayList<>();
        if(null!=getRoleRequests){
            roles.addAll(roleRepository.findAllById(getRoleRequests.stream().map(GetRoleRequest::getRoleId).distinct().collect(Collectors.toList())));
            roles.addAll(roleRepository.findAllByRoleIn(getRoleRequests.stream().map(GetRoleRequest::getRoleName).distinct().collect(Collectors.toList())));
            roles = removeDuplicates(roles);
        }
        else{
            throw new RuntimeException("No Role provided");
        }
        return roles;
    }

    @PostMapping("/role")
    @ResponseBody
    public Role saveRole(@RequestBody Role role) throws Exception {
        role.setRole(role.getRole().toUpperCase());
        if(!roleRepository.existsByName(role.getRole())){
            return roleRepository.save(role);
        } else throw new Exception("Role already exists");
    }

    private  <T> List<T> removeDuplicates(List<T> list) {
        // Convert the list to a Set (which automatically removes duplicates)
        HashSet<T> set = new HashSet<>();
        List<T> list1 = new ArrayList<>();
        for(T tuple: list){
            if(!set.contains(tuple)){
                set.add(tuple);
                list1.add(tuple);
            }
        }
        return list1;
    }
}
