package org.ying.book.dto.common;


public interface IPageReq {
    Integer pageSize = 10;
    Integer current = 1;

    Integer getPageSize();
    Integer getCurrent();
}
