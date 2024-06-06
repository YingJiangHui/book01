package org.ying.book.dto.library;

import lombok.Getter;
import lombok.Setter;
import org.ying.book.dto.common.IPageReq;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.pojo.Library;

@Setter
@Getter
public class BooksInLibraryDto implements IPageReq {
    private Integer pageSize = 10;
    private Integer current = 1;
}
