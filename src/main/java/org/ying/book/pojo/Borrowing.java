package org.ying.book.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrowing {
    private Integer id;

    private Integer userId;

    private Integer bookId;

    private Date borrowedAt;

    private Date expectedReturnAt;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;
}