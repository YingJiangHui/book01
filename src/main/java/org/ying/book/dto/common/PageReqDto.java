package org.ying.book.dto.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageReqDto implements IPageReq{
    private Integer pageSize;
    private Integer current;
    public PageReqDto() {
        this.pageSize = 10; // 默认页码为1
        this.current = 1; // 默认每页显示10条数据
    }
}