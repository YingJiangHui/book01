package org.ying.book.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Role {
    private Integer id;

    private String roleName;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

}