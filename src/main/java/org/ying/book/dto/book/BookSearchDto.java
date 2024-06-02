package org.ying.book.dto.book;

import lombok.Data;
import org.ying.book.dto.common.IPageReq;
import org.ying.book.pojo.Book;

import java.util.List;

@Data
public class BookSearchDto extends Book implements IPageReq {
    String keyword;
    Integer libraryId;
    List<Integer> ids;
    private Integer pageSize = 10;
    private Integer current = 1;
}
