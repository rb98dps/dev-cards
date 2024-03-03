package com.devapi.services;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.Exceptions.GeneralExceptions;
import com.devapi.Exceptions.RoleException;
import com.devapi.dao.RoleRepository;
import com.devapi.entities.Role;
import com.devapi.requestentities.CreateRoleRequest;
import com.devapi.requestentities.GetRoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;
    private static final String DEFAULT_ROLE = "GUEST";
    @Override
    public List<Role> getDefaultRole() throws CustomDevApiException {
        List<Role> roles = roleRepository.findByRoleName(DEFAULT_ROLE);
        if(roles.isEmpty()){
           roles = Collections.singletonList(createRole(new CreateRoleRequest(DEFAULT_ROLE)));
        }
        return roles;
    }

    public List<Role> getRole(GetRoleRequest getRoleRequest) throws CustomDevApiException {
        List<Role> roles;
        if (null != getRoleRequest.getRoleId()) {
            roles = roleRepository.findAllById(Collections.singletonList(getRoleRequest.getRoleId()));
        } else if (null != getRoleRequest.getRoleName()) {
            roles = roleRepository.findByRoleName(getRoleRequest.getRoleName());
        } else {
            throw new CustomDevApiException(GeneralExceptions.BadRequestException);
        }
        return roles;
    }
    public Role createRole(CreateRoleRequest createRoleRequest) throws CustomDevApiException {
        String roleName;
        if(createRoleRequest.getRole()==null)
        {
            throw new CustomDevApiException(GeneralExceptions.BadRequestException);
        }

        roleName = createRoleRequest.getRole();
        roleName = roleName.toUpperCase();
        createRoleRequest.setRole(roleName);
        Role savedRole;
        if (!roleRepository.existsByName(roleName)) {
            savedRole = roleRepository.save(new Role(roleName));
        } else {
            throw new CustomDevApiException(RoleException.ROLE_ALREADY_EXIST);
        }
        return savedRole;
    }

    public List<Role> getRoles( List<GetRoleRequest> getRoleRequests){
        List<Role> roles = new ArrayList<>();
        if (null != getRoleRequests) {
            roles.addAll(roleRepository.findAllById(getRoleRequests.stream().map(GetRoleRequest::getRoleId).distinct().collect(Collectors.toList())));
            roles.addAll(roleRepository.findAllByRoleIn(getRoleRequests.stream().map(GetRoleRequest::getRoleName).distinct().collect(Collectors.toList())));
            roles = removeDuplicates(roles);
        }
        return roles;
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
