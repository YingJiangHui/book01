package org.ying.book.dto.user;

import lombok.Builder;
import lombok.Data;
import org.ying.book.enums.RoleEnum;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class JwtDTO {
    int id;
    String email;
    List<RoleEnum> roles;
    private Date createdAt;
}
