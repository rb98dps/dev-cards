package com.devapi.services;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.entities.Role;
import com.devapi.entities.User;
import com.devapi.requestentities.AssignRoleRequest;
import com.devapi.requestentities.CreateUserRequest;
import com.devapi.requestentities.GetUserRequest;
import com.devapi.responseObjects.UserDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user);

    User update(User user);

    Optional<User> findById(UUID userId);

    Optional<User> findByEmail(String email) throws Exception;
    List<User> findAll();

    boolean deleteById(UUID userId);

    boolean deleteByEmail(String userId) throws Exception;
    User save(CreateUserRequest createUserRequest) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, CustomDevApiException;

    // Method to update only the provided items in the request body using class properties
    User updateUser(String username, UserDTO userDTO) throws Exception;

    void addRolesToUser(User user, List<Role> roles) throws Exception;

    User getDummyUser() throws Exception;

    public User findByEmailOrId(GetUserRequest getUserRequest) throws Exception;

    public User assignRoles(AssignRoleRequest assignRoleRequest) throws Exception;
}
