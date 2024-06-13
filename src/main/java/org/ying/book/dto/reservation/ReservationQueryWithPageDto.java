package org.ying.book.dto.reservation;

import lombok.Data;
import org.ying.book.dto.common.PageReqDto;

@Data
public class ReservationQueryWithPageDto extends PageReqDto {
    private Integer userId;
    private Integer bookId;
    private Integer id;
    private String status;
    private String createdAt;
    private String borrowedAt;
    private String title;
    private String email;
    private Integer libraryId;
}
