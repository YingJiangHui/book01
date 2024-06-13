package org.ying.book.dto.user;

import lombok.*;
import org.ying.book.enums.RoleEnum;
import org.ying.book.pojo.Library;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ContextUserDto extends UserJwtDto{

    List<Library> managedLibraries;

    public ContextUserDto(Integer id, String email, boolean isBlacklist, Integer defaultTimes, List<RoleEnum> roles, Date createdAt, List<Integer> managedLibraries) {
        super(id, email, isBlacklist, defaultTimes, roles, createdAt, managedLibraries);
    }

}
