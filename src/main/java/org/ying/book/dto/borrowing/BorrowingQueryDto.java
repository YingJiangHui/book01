package org.ying.book.dto.borrowing;

import lombok.Data;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.enums.BorrowingStatusEnum;

import java.util.List;

@Data
public class BorrowingQueryDto extends PageReqDto {
    List<BorrowingStatusEnum> status;
    Integer userId;
}
