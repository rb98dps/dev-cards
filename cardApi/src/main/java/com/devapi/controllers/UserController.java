package com.devapi.controllers;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.Exceptions.UserException;
import com.devapi.services.UserService;
import com.devapi.util.DevApiUtilities;
import com.devapi.entities.User;
import com.devapi.requestentities.AssignRoleRequest;
import com.devapi.requestentities.CreateUserRequest;
import com.devapi.requestentities.DeleteUserRequest;
import com.devapi.requestentities.GetUserRequest;
import com.devapi.responseObjects.BasicResponse;
import com.devapi.responseObjects.Response;
import com.devapi.responseObjects.UserDTO;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final String DEFAULT_ROLE = "GUEST";
    UserService userService;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/get_user")
    public Response getUser(@RequestBody @NonNull GetUserRequest getUserRequest) throws Exception {
        User user = userService.findByEmailOrId(getUserRequest);
        if (user == null) {
            throw new CustomDevApiException(UserException.USER_NOT_FOUND);
        }
        UserDTO userDTO = DevApiUtilities.mapObjectToObjectWithModelMapper(user, UserDTO.class);
        return new BasicResponse<>("success", HttpStatus.OK.value(), userDTO);
    }

    @PostMapping("/user")
    public Response saveUser(@RequestBody @NonNull CreateUserRequest createUserRequest) throws Exception {
        User user = userService.save(createUserRequest);
        return new BasicResponse<>("success", HttpStatus.OK.value(), DevApiUtilities.mapObjectToObjectWithModelMapper(user, UserDTO.class));
    }

    @GetMapping("/users")
    public Response getUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = users.stream().map(user -> DevApiUtilities.mapObjectToObjectWithModelMapper(user, UserDTO.class)).toList();
        return new BasicResponse<>("success", HttpStatus.OK.value(), userDTOs);
    }

    @PutMapping("/user")
    public Response updateUser(@RequestBody UserDTO userdto) throws Exception {
        User user = userService.updateUser(userdto.getEmail(), userdto);
        UserDTO userDTO = DevApiUtilities.mapObjectToObjectWithModelMapper(user, UserDTO.class);
        return new BasicResponse<>("success", HttpStatus.OK.value(), userDTO);
    }

    @DeleteMapping("/user")
    public Response deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) throws Exception {
        Optional<User> optionalUser = userService.findByEmail(deleteUserRequest.getEmail());
        User user = optionalUser.orElseThrow(() -> new CustomDevApiException(UserException.USER_NOT_FOUND));
        return new BasicResponse<>("success",
                HttpStatus.OK.value());
    }

    @PostMapping("/user/assign-roles")
    public Response assignRole(@RequestBody AssignRoleRequest assignRoleRequest) throws Exception {
        User user = userService.assignRoles(assignRoleRequest);
        UserDTO userDTO = DevApiUtilities.mapObjectToObjectWithModelMapper(user, UserDTO.class);
        return new BasicResponse<>("success", HttpStatus.OK.value(), userDTO);
    }
}

