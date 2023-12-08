package com.devapi.controllers;

import com.devapi.DTOs.UserDTO;
import com.devapi.Exceptions.UserException;
import com.devapi.Exceptions.UserExceptionResponse;
import com.devapi.model.entities.Role;
import com.devapi.model.entities.User;
import com.devapi.model.requestentities.AssignRoleRequest;
import com.devapi.model.requestentities.CreateUserRequest;
import com.devapi.model.requestentities.DeleteUserRequest;
import com.devapi.model.requestentities.GetUserRequest;
import com.devapi.responseObjects.Response;
import com.devapi.responseObjects.StandardUserResponse;
import com.devapi.services.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    UserService userService;

    @Autowired
    RoleController roleController;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/getUser")
    public ResponseEntity<Response> getUser(@RequestBody @NonNull GetUserRequest getUserRequest) throws Exception {
        User user;
        if (null != getUserRequest.getId()) {
            user = userService.findById(getUserRequest.getId()).orElse(null);
        } else if (null != getUserRequest.getEmail()) {
            user = userService.findByEmail(getUserRequest.getEmail()).orElse(null);
        } else {
            return new ResponseEntity<>(new UserExceptionResponse(
                    "Necessary Fields not found", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        if (null != user) {
            return new ResponseEntity<>(new StandardUserResponse("success", HttpStatus.OK.value(),
                    System.currentTimeMillis(), user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new UserExceptionResponse(UserException.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND), HttpStatus.OK);
        }

    }

    @PostMapping("/user")
    public ResponseEntity<Response> saveUser(@RequestBody CreateUserRequest createUserRequest) throws IllegalAccessException {
        User user = userService.save(createUserRequest);
        user.setPassword("");
        return new ResponseEntity<>(new StandardUserResponse("success", HttpStatus.OK.value(),
                System.currentTimeMillis(), user), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Response> getUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(new StandardUserResponse("success", HttpStatus.OK.value(),
                System.currentTimeMillis(), users), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<Response> updateUser(@RequestBody UserDTO userdto) throws Exception {
        User user = userService.updateUser(userdto.getEmail(), userdto);
        return new ResponseEntity<>(new StandardUserResponse("success", HttpStatus.OK.value(),
                System.currentTimeMillis(), user), HttpStatus.OK);
    }

    @DeleteMapping("/user")
    public ResponseEntity<Response> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) throws Exception {

        Optional<User> optionalUser = userService.findByEmail(deleteUserRequest.getEmail());

        User user = optionalUser.orElse(null);
        assert user != null;
        boolean deleted = userService.deleteById(user.getId());
        if (Boolean.TRUE.equals(deleted)) {
            return new ResponseEntity<>(new StandardUserResponse("success",
                    HttpStatus.OK.value(), System.currentTimeMillis()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new UserExceptionResponse(UserException.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/user/assign-roles")
    public ResponseEntity<Response> assignRole(@RequestBody AssignRoleRequest assignRoleRequest) throws Exception {
        GetUserRequest getUserRequest = new GetUserRequest(assignRoleRequest.getUserId(), assignRoleRequest.getUserEmail());
        ResponseEntity<Response> responseEntity = getUser(getUserRequest);
        Response userResponse = responseEntity.getBody();
        List<Role> roles = roleController.getRoles(assignRoleRequest.getAssignRoles());
        if (roles.size() == 0)
            return new ResponseEntity<>(new UserExceptionResponse("no Roles found with given name",
                    HttpStatus.NOT_FOUND), HttpStatus.BAD_REQUEST);
        if (userResponse instanceof StandardUserResponse) {
            User user = ((StandardUserResponse) userResponse).getMainObject();
            userService.addRolesToUser(user, roles);
        }
        return responseEntity;
    }
}

