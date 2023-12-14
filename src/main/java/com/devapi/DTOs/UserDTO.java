package com.devapi.DTOs;

import lombok.*;

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
