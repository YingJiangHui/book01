package org.ying.book.dto.email;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailBorrowNotificationDto {
    String email;
    String libraryLocationLink;
    String libraryName;
    Integer days;
    String bookName;
}
