package org.ying.book.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String email;

    private String password;

    private String passwordConfirmation;

    private String validationCode;
}
