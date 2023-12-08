package com.devapi.DTOs;

import com.devapi.model.entities.Role;
import lombok.*;
import org.hibernate.Hibernate;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class UserDTO {

    String firstName;

    String lastName;

    String password;

    @NonNull
    String email;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
