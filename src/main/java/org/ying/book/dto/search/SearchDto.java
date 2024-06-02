package org.ying.book.dto.search;

import lombok.Data;

@Data
public class SearchDto {
    private String target;
    private Integer userId;
    private String keyword;
}
