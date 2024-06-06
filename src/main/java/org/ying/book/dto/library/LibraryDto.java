package org.ying.book.dto.library;

import lombok.Data;
import org.ying.book.dto.common.IPageReq;
import org.ying.book.pojo.Library;

@Data
public class LibraryDto extends Library implements IPageReq {

    private Integer pageSize = 10;
    private Integer current = 1;
}
