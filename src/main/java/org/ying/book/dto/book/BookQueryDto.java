package org.ying.book.dto.book;

import lombok.Data;
import org.ying.book.dto.common.IPageReq;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.pojo.Book;

import java.util.List;

@Data
public class BookQueryDto extends Book implements IPageReq {
    Integer pageSize = 10;
    Integer current = 1;
    List<Integer> ids;
    Integer firstLibraryId;
}
