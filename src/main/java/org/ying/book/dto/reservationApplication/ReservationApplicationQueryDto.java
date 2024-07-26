package org.ying.book.dto.reservationApplication;

import lombok.Builder;
import lombok.Data;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.enums.ReservationApplicationEnum;

import java.util.List;

@Data
@Builder
public class ReservationApplicationQueryDto extends PageReqDto {
    Integer bookId;
    List<ReservationApplicationEnum> status;
    String title;
    String email;
    Integer libraryId;
    Integer userId;
    String orderByClause;
}
