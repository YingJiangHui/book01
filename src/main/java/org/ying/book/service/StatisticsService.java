package org.ying.book.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.ying.book.dto.statistics.StatisticsQueryDto;
import org.ying.book.mapper.BorrowingMapper;
import org.ying.book.mapper.SearchMapper;
import org.ying.book.pojo.*;
import org.ying.book.utils.PaginationHelper;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticsService {
    @Resource
    BorrowingMapper borrowingMapper;
    @Resource
    SearchMapper searchMapper;

    private void borrowingWithExample(BorrowingExample borrowingExample, StatisticsQueryDto statisticsQueryDto) {
        Optional.ofNullable(statisticsQueryDto).ifPresent((statisticsQuery) -> {
            if (statisticsQuery.getStartTime() != null) {
                borrowingExample.createCriteria().andBorrowedAtGreaterThan(statisticsQuery.getStartTime());
            }

            if (statisticsQuery.getEndTime() != null) {
                borrowingExample.createCriteria().andBorrowedAtLessThan(statisticsQuery.getEndTime());
            }
        });
    }

    public List<HotRankStatisticsEntity> getHotBorrowedBooks(StatisticsQueryDto statisticsQueryDto) {
        BorrowingExample borrowingExample = new BorrowingExample();
        borrowingWithExample(borrowingExample, statisticsQueryDto);
        return PaginationHelper.paginate(statisticsQueryDto, (rowBounds,queryDto)-> borrowingMapper.selectHotBorrowedBooks(borrowingExample,rowBounds));
    }

    public List<HotRankStatisticsEntity> getHotBorrowedCategories(StatisticsQueryDto statisticsQueryDto) {
        BorrowingExample borrowingExample = new BorrowingExample();
        borrowingWithExample(borrowingExample, statisticsQueryDto);
        return PaginationHelper.paginate(statisticsQueryDto, (rowBounds,queryDto)-> borrowingMapper.selectHotBorrowedCategories(borrowingExample,rowBounds));
    }

    public List<HotRankStatisticsEntity> getHotBorrowedLibraries(StatisticsQueryDto statisticsQueryDto) {
        BorrowingExample borrowingExample = new BorrowingExample();
        borrowingWithExample(borrowingExample, statisticsQueryDto);
        return PaginationHelper.paginate(statisticsQueryDto, (rowBounds,queryDto)-> borrowingMapper.selectHotBorrowedLibraries(borrowingExample,rowBounds));
    }

    public List<HotRankStatisticsEntity> selectHotSearchText(StatisticsQueryDto statisticsQueryDto) {
        SearchExample searchExample = new SearchExample();
        return PaginationHelper.paginate(statisticsQueryDto, (rowBounds,queryDto)-> searchMapper.selectHotSearchText(searchExample,rowBounds));
    }

}
