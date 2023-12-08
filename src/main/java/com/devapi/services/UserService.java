package com.devapi.services;

import com.devapi.DTOs.UserDTO;
import com.devapi.model.entities.Role;
import com.devapi.model.entities.User;
import com.devapi.model.requestentities.CreateUserRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

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
    User save(CreateUserRequest createUserRequest) throws IllegalAccessException;

    // Method to update only the provided items in the request body using class properties
    User updateUser(String username, UserDTO userDTO) throws Exception;

    @Transactional
    void addRolesToUser(User user, List<Role> roles) throws Exception;
}
