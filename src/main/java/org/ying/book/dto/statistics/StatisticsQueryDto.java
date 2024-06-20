package org.ying.book.dto.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.ying.book.dto.common.PageReqDto;

import java.util.Date;

@Data
@Builder
public class StatisticsQueryDto extends PageReqDto implements IStatisticsQueryDto{
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    Date startTime = null;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    Date endTime = null;
    Integer libraryId;

    String dateTrunc="day";
}
