package org.ying.book.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowingView {
    private Integer id;

    private Integer userId;

    private Integer bookId;

    private Date borrowedAt;

    private Date returnedAt;

    private Date expectedReturnAt;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

    private String status;

    private Book book;
//    最迟可能归还的时间
    private Date latestReturnAt;
}