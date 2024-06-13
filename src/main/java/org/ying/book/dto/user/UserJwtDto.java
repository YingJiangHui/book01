package org.ying.book.dto.user;

import lombok.Builder;
import lombok.Data;
import org.ying.book.enums.RoleEnum;
import org.ying.book.pojo.Library;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class UserJwtDto {
    Integer id;
    String email;
    boolean isBlacklist;
    Integer defaultTimes;
    List<RoleEnum> roles;
    private Date createdAt;
    List<Integer> managedLibraries;


    public boolean isSystemAdmin() {
        return roles.contains(RoleEnum.SYSTEM_ADMIN);
    }

    public boolean isLibraryAdmin() {
        return roles.contains(RoleEnum.LIBRARY_ADMIN);
    }

    public boolean isReader() {
        return roles.contains(RoleEnum.READER);
    }

    public boolean isLibraryAdminOnly() {
        return !this.isSystemAdmin()&&this.isLibraryAdmin();
    }

    public boolean isReaderOnly(){
        return !this.isSystemAdmin()&&!this.isLibraryAdmin()&&this.isReader();
    }
}
