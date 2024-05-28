package org.ying.book.dto.borrowing;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.ying.book.enums.ActionSource;

import java.util.Date;
import java.util.List;

@Data
public class BorrowingDto {
    List<Integer> bookIds;
    Integer userId;
    @NotNull
    Date borrowedAt;
    @NotNull
    Date expectedReturnAt;

    ActionSource from = ActionSource.DETAIL;
}
