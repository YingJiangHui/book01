package org.ying.book.dto.book;

import lombok.Data;
import org.ying.book.dto.common.PageReqDto;

import java.util.List;

@Data
public class BookQueryDto extends PageReqDto {
    List<Integer> ids;
    Integer firstLibraryId;
}
