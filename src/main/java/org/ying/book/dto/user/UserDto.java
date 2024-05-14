package org.ying.book.dto.user;

import lombok.Data;

@Data
public class UserDto {
    private String email;

    private String password;

    private String passwordConfirmation;

    private String validationCode;
}
