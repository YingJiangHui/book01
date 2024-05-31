package org.ying.book.dto.reservation;

import lombok.Data;
import org.ying.book.enums.ReservationStatusEnum;

@Data
public class ReservationQueryDto{
    private Integer userId;
    private Integer bookId;
    private Integer id;
    private String status;
    private String createdAt;
    private String borrowedAt;
}
