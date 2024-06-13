package org.ying.book.dto.statistics;

import lombok.Builder;
import lombok.Data;
import org.ying.book.dto.common.PageReqDto;

import java.util.Date;

@Data
@Builder
public class StatisticsQueryDto extends PageReqDto implements IStatisticsQueryDto{
    Date startTime = null;
    Date endTime = null;
}
