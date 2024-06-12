package org.ying.book.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.enums.RoleEnum;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class UserQueryParamsDTO extends PageReqDto {
    String email;
    List<RoleEnum> roleNames;
    List<Integer> libraryIds;
    Boolean isBlacklist;
    Integer id;
}
