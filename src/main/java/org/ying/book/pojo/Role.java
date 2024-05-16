package org.ying.book.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ying.book.enums.RoleEnum;

import java.util.Date;

@Setter
@Getter
@Builder
public class Role {
    private Integer id;

    private RoleEnum roleName;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

}