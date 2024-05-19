package org.ying.book.dto.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ying.book.enums.RoleEnum;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
public class EmailValidationDto implements Serializable {
    String email;

    List<RoleEnum> roles;

    List<Integer> libraryIds;

    String code;
}
