package com.devapi.controllers;

import com.devapi.Exceptions.UserException;
import com.devapi.Exceptions.UserExceptionResponse;
import com.devapi.model.entities.Role;
import com.devapi.model.entities.User;
import com.devapi.model.requestentities.AssignRoleRequest;
import com.devapi.model.requestentities.CreateUserRequest;
import com.devapi.model.requestentities.DeleteUserRequest;
import com.devapi.model.requestentities.GetUserRequest;
import com.devapi.responseObjects.*;
import com.devapi.services.UserService;
import com.devapi.util.DevApiUtilities;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final String DEFAULT_ROLE = "GUEST";
    UserService userService;

    @Autowired
    RoleController roleController;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/get_user")
    public ResponseEntity<Response> getUser(@RequestBody @NonNull GetUserRequest getUserRequest) throws Exception {
        User user = userService.findByEmailOrId(getUserRequest);
        if (user == null) {
            return new ResponseEntity<>(new UserExceptionResponse(UserException.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND), HttpStatus.OK);
        }
        UserDTO userDTO = DevApiUtilities.mapObjectToObjectWithModelMapper(user, UserDTO.class);
        return new ResponseEntity<>(new BasicResponse<>("success", HttpStatus.OK.value(), userDTO), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<Response> saveUser(@RequestBody @NonNull CreateUserRequest createUserRequest) throws Exception {

        User user = userService.save(createUserRequest);
        List<Role> roles = ((StandardRoleResponse) Objects.requireNonNull(roleController.getRole(null, DEFAULT_ROLE).getBody())).getMainObjects();
        userService.addRolesToUser(user, roles);
        return new ResponseEntity<>(new BasicResponse<>("success", HttpStatus.OK.value(), DevApiUtilities.mapObjectToObjectWithModelMapper(user, UserDTO.class)), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Response> getUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOS = users.stream().map(user -> DevApiUtilities.mapObjectToObjectWithModelMapper(user, UserDTO.class)).toList();
        return new ResponseEntity<>(new BasicResponse<>("success", HttpStatus.OK.value(), userDTOS), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<Response> updateUser(@RequestBody UserDTO userdto) throws Exception {
        User user = userService.updateUser(userdto.getEmail(), userdto);
        UserDTO userDTO = DevApiUtilities.mapObjectToObjectWithModelMapper(user, UserDTO.class);
        return new ResponseEntity<>(new BasicResponse<>("success", HttpStatus.OK.value(), userDTO), HttpStatus.OK);
    }

    @DeleteMapping("/user")
    public ResponseEntity<Response> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) throws Exception {

        Optional<User> optionalUser = userService.findByEmail(deleteUserRequest.getEmail());

        User user = optionalUser.orElse(null);
        assert user != null;
        boolean deleted = userService.deleteById(user.getId());
        if (Boolean.TRUE.equals(deleted)) {
            return new ResponseEntity<>(new StandardUserResponse("success",
                    HttpStatus.OK.value()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new UserExceptionResponse(UserException.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/user/assign-roles")
    public ResponseEntity<Response> assignRole(@RequestBody AssignRoleRequest assignRoleRequest) throws Exception {
        GetUserRequest getUserRequest = new GetUserRequest(assignRoleRequest.getUserId(), assignRoleRequest.getUserEmail());
        User user = userService.findByEmailOrId(getUserRequest);
        ResponseEntity<Response> roleResponseEntity = roleController.getRoles(assignRoleRequest.getAssignRoles());
        Response roleResponse = roleResponseEntity.getBody();
        if (!(roleResponse instanceof StandardRoleResponse))
            return roleResponseEntity;
        if (user != null) {
            List<Role> roles = ((StandardRoleResponse) roleResponse).getMainObjects();
            userService.addRolesToUser(user, roles);
            UserDTO userDTO = DevApiUtilities.mapObjectToObjectWithModelMapper(user, UserDTO.class);
            return new ResponseEntity<>(new BasicResponse<>("success", HttpStatus.OK.value(), userDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new UserExceptionResponse(UserException.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND), HttpStatus.OK);
        }
    }
}

