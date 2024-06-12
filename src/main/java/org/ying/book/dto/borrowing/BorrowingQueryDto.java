package org.ying.book.dto.borrowing;

import lombok.Builder;
import lombok.Data;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.enums.BorrowingStatusEnum;

import java.util.List;

@Data
@Builder
public class BorrowingQueryDto extends PageReqDto {
    List<BorrowingStatusEnum> status;
    Integer userId;
    Integer libraryId;
    Integer bookId;
    String title;
    String email;
}
