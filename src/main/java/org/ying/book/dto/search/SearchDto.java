package org.ying.book.dto.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import org.ying.book.pojo.Search;

@Data
public class SearchDto extends Search {
    private Integer size;
}
