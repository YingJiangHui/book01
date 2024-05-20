package org.ying.book.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Book {

    private Integer id;

    private String title;

    private String author;

    private Integer categoryId;

    private Integer publishedYear;

    private String isbn;

    private Boolean available;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

    private String files;

    private String description;
}