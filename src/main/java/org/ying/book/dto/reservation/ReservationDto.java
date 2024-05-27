package org.ying.book.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReservationDto {
    List<Integer> bookIds;
    Integer userId;
    @NotNull
    Date borrowedAt;
    @NotNull
    Date expectedReturnAt;
}