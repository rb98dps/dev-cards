package com.devapi.services;

import com.devapi.DTOs.UserDTO;
import com.devapi.dao.Dao;
import com.devapi.model.entities.Role;
import com.devapi.model.entities.User;
import com.devapi.model.requestentities.CreateUserRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserServiceImpl extends GenericServiceImpl<User, UUID> implements UserService {


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
    public User save(CreateUserRequest createUserRequest) throws IllegalAccessException {
        //PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            if (checkExistingUser(createUserRequest.getClass().getDeclaredField("email").getName(), createUserRequest.getEmail()))
                throw new Exception("User already exist in database");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        User user = new User();
        user = mapObjectToUser(createUserRequest, user);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
        System.out.println(user);
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
                User user = mapObjectToUser(userDTO, userList.get(0));
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

    public User mapObjectToUser(Object object, User user) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();

        // For each field, set the value of the corresponding field in the item object
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(object) != null) {
                Field itemField;
                try {
                    itemField = user.getClass().getDeclaredField(field.getName());
                } catch (NoSuchFieldException e) {
                    continue;
                }
                itemField.setAccessible(true);
                itemField.set(user, field.get(object));
            }
        }
        return user;
    }
}
