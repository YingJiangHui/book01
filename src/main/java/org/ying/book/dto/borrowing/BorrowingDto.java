package org.ying.book.dto.borrowing;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BorrowingDto {
    List<Integer> bookIds;
    Integer userId;
    Date borrowedAt;
    Date expectedReturnAt;
}
