package org.ying.book.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ReservationApplication {
    private Integer id;

    private Integer userId;

    private Integer borrowingId;

    private Integer bookId;

    private Book book;

    private User user;

    private String status;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;
}