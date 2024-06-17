package org.ying.book.dto.reservationApplication;

import lombok.Builder;
import lombok.Data;
import org.ying.book.enums.ReservationApplicationEnum;

import java.util.List;

@Data
@Builder
public class ReservationApplicationQueryDto {
    Integer bookId;
    List<ReservationApplicationEnum> status;
}
