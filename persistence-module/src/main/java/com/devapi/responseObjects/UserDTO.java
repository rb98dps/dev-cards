package com.devapi.responseObjects;

import com.devapi.entities.Role;
import lombok.*;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class UserDTO {

    String firstName;

    String lastName;

    @NonNull
    String email;

    private Collection<Role> roles;

    Timestamp createTime;

    Timestamp lastLoginDate;
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
