package org.ying.book.dto.category;

import lombok.Data;
import org.ying.book.dto.common.PageReqDto;

@Data
public class CategoryQueryDto extends PageReqDto {
    Integer firstLibraryId;
}
