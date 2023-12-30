package com.devapi.services;

import com.devapi.model.requestentities.GetUserRequest;
import com.devapi.responseObjects.UserDTO;
import com.devapi.dao.Dao;
import com.devapi.model.entities.Role;
import com.devapi.model.entities.User;
import com.devapi.model.requestentities.CreateUserRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.devapi.util.DevApiUtilities.mapObjectToObject;

@Component
public class UserServiceImpl extends GenericServiceImpl<User, UUID> implements UserService {


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(Dao<User, UUID> dao) {
        super(dao);
        this.dao.setClazz(User.class);
        System.out.println(dao.hashCode());
    }


    @Override
    public Optional<User> findByEmail(String email) throws Exception {
        List<User> userList = getUsersBySingleAttribute(User.class.getDeclaredField("email").getName(), email);
        int users = userList.size();
        return switch (users) {
            case 0 -> throw new Exception("No user found with the email id");
            case 1 -> Optional.of(userList.get(0));
            default -> throw new Exception("More than 1 user found with the mail id");
        };
    }

    @Override
    public boolean deleteByEmail(String email) throws Exception {
        List<User> userList = getUsersBySingleAttribute(User.class.getDeclaredField("email").getName(), email);
        int users = userList.size();
        return switch (users) {
            case 0 -> throw new Exception("No user found with the email id");
            case 1 -> deleteById(userList.get(0).getId());
            default -> throw new Exception("More than 1 user found with the mail id");
        };
    }

    @Override
    @Transactional
    public User save(CreateUserRequest createUserRequest) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            if (checkExistingUser(createUserRequest.getClass().getDeclaredField("email").getName(), createUserRequest.getEmail()))
                throw new Exception("User already exist in database");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        createUserRequest.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        User user;
        user = (User) mapObjectToObject(createUserRequest, User.class);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
        user.setIsActive(true);
        return this.dao.save(user);
    }

    private boolean checkExistingUser(String attributeName, String value) {
        return this.dao.checkEntity(attributeName, value);
    }

    private List<User> getUsersBySingleAttribute(String attributeName, String value) {
        return this.dao.getEntity(attributeName, value);
    }

    @Override
    @Transactional
    public User updateUser(String email, UserDTO userDTO) throws Exception {

        if (null == email)
            throw new Exception("Email required for update");
        List<User> userList = getUsersBySingleAttribute(userDTO.getClass().getDeclaredField("email").getName(), userDTO.getEmail());
        int users = userList.size();
        switch (users) {
            case 0 -> throw new Exception("No user found with the email id");
            case 1 -> {
                User user = (User) mapObjectToObject(userDTO, userList.get(0));
                return this.dao.save(user);
            }
            default -> throw new Exception("More than 1 user found with the mail id");
        }
    }

    @Override
    @Transactional
    public void addRolesToUser(User user, List<Role> roles) {
        user.addRoles(roles);
    }

    @Override
    public User getDummyUser() throws Exception {
        Optional<User> user = this.findByEmail("dummy@gmail.com");
        if(user.isPresent()){
            return user.get();
        }
        throw new Exception("Some problem occurred while fetching dummy user");
    }

    public User findByEmailOrId(GetUserRequest getUserRequest) throws Exception {
        User user = null;
        if (null != getUserRequest.getId()) {
            user = findById(getUserRequest.getId()).orElse(null);
        } else if (null != getUserRequest.getEmail()) {
            user = findByEmail(getUserRequest.getEmail()).orElse(null);
        } else {
            throw new Exception("Bad Request, Necessary Fields not found");
        }
        return user;
    }

}
