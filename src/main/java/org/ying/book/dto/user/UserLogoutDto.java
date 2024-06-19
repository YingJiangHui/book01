package org.ying.book.dto.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserLogoutDto  implements Serializable {
    String message;
    Object token;
    String email;
    Boolean infoChanged;
}
