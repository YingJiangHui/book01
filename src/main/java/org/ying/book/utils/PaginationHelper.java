package org.ying.book.utils;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;

import java.util.List;
import java.util.function.BiFunction;

public class PaginationHelper {

    public static <T> PageResultDto<T> paginate(PageReqDto pageReqDto, BiFunction<RowBounds, PageReqDto, List<T>> queryFunction, long total) {
        RowBounds rowBounds = new RowBounds(pageReqDto.getCurrent(), pageReqDto.getPageSize());
        List<T> data = queryFunction.apply(rowBounds, pageReqDto);
        return new PageResultDto<>(data, total);
    }
}