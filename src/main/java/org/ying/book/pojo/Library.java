package org.ying.book.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class Library {
    private Integer id;

    private String name;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Integer circumference;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

    private String address;

    private Integer maxBorrowDays;

    private Integer defaultBorrowDays;

    private Boolean closed;
}