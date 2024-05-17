package org.ying.book.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Library {
    private Integer id;

    private String name;

    private Integer latitude;

    private Integer longitude;

    private Integer circumference;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

}