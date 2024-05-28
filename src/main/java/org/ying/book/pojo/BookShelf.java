package org.ying.book.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class BookShelf {
    private Integer id;

    private Integer userId;

    private Integer bookId;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

}