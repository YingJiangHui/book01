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

    private BorrowingExample.Criteria borrowingWithExample(BorrowingExample borrowingExample, StatisticsQueryDto statisticsQueryDto) {
        BorrowingExample.Criteria criteria = borrowingExample.createCriteria();

        Optional.ofNullable(statisticsQueryDto).ifPresent((statisticsQuery) -> {
            if (statisticsQuery.getStartTime() != null) {
                criteria.andBorrowedAtBetween(statisticsQuery.getStartTime(),statisticsQuery.getEndTime());
            }
            if (statisticsQuery.getLibraryId() != null){
                criteria.andLibraryIdEqualTo(statisticsQuery.getLibraryId());
            }
        });
        return criteria;
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
        return PaginationHelper.paginate(statisticsQueryDto, (rowBounds,queryDto)-> borrowingMapper.selectHotBorrowedLibraries(borrowingExample,statisticsQueryDto.getDateTrunc(),rowBounds));
    }

    public List<HotRankStatisticsEntity> selectHotSearchText(StatisticsQueryDto statisticsQueryDto) {
        SearchExample searchExample = new SearchExample();
        Optional.ofNullable(statisticsQueryDto).ifPresent((statisticsQuery) -> {
            SearchExample.Criteria criteria = searchExample.createCriteria();
            if (statisticsQuery.getStartTime() != null&&statisticsQuery.getEndTime() != null){
                criteria.andCreatedAtBetween(statisticsQuery.getStartTime(),statisticsQuery.getEndTime());
            }

        });


        return PaginationHelper.paginate(statisticsQueryDto, (rowBounds,queryDto)-> searchMapper.selectHotSearchText(searchExample,rowBounds));
    }

}
