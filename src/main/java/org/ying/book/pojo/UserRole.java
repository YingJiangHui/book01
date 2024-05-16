package org.ying.book.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
public class UserRole {
    private Integer id;

    private Integer userId;

    private Integer roleId;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

}