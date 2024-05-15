package org.ying.book.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class User {
    private Integer id;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

    List<Role> roles;
}