package org.ying.book.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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