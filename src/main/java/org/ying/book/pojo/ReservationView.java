package org.ying.book.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationView {
    private Integer id;

    private Integer userId;

    private Integer bookId;

    private Date borrowedAt;

    private Date returnedAt;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

    private Integer borrowingId;

    private String status;

    private Book book;
}