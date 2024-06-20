package org.ying.book.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HotRankStatisticsEntity {
    Integer id;
    String name;
    Integer count;
    Date dateRange;
}
