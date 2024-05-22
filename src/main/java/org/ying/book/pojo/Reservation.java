package org.ying.book.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Integer id;

    private Integer userId;

    private Integer bookId;

    private Boolean fulfilled;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

    private Date returnedAt;

    private Date borrowedAt;


}