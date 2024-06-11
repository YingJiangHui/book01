package org.ying.book.dto.statistics;

import java.util.Date;

public interface IStatisticsQueryDto {
    Date StartTime = null;
    Date EndTime= null;

    Date getStartTime();
    Date getEndTime();


}
