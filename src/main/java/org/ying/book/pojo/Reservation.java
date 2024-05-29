package org.ying.book.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ying.book.enums.ReservationStatusEnum;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    private Integer id;

    private Integer userId;

    private Integer bookId;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

    private Date returnedAt;

    private Date borrowedAt;

    private ReservationStatusEnum status;
}