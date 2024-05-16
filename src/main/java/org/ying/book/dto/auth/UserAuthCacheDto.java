package org.ying.book.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserAuthCacheDto {
    String email;

    List<Integer> roleIds;

    List<Integer> libraryIds;

    String code;
}
