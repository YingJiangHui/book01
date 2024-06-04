package org.ying.book.dto.user;

import lombok.Data;
import org.ying.book.enums.RoleEnum;

import java.util.List;

@Data
public class UserUpdateDto {
    List<RoleEnum> roles;
    Boolean isBlacklist;
    List<Integer> libraryIds;
}
