package org.ying.book.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.ying.book.dto.statistics.StatisticsQueryDto;
import org.ying.book.pojo.HotRankStatisticsEntity;

import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
public class StatisticsServiceTests {
    @Resource
    StatisticsService statisticsService;

    @Test
    public void testHotBorrowedBooks() {
         List<HotRankStatisticsEntity> borrowingHotList = statisticsService.getHotBorrowedBooks(StatisticsQueryDto.builder().build());
        log.info(String.valueOf(borrowingHotList.size()));
    }

    @Test
    public void testHotBorrowedCategory() {
        List<HotRankStatisticsEntity> categoryHots = statisticsService.getHotBorrowedCategories(StatisticsQueryDto.builder().startTime(new Date()).endTime(new Date()).build());
        log.info(String.valueOf(categoryHots.size()));
    }

    @Test
    public void testHotBorrowedLibraries() {
        List<HotRankStatisticsEntity> libraryHots = statisticsService.getHotBorrowedLibraries(StatisticsQueryDto.builder().startTime(new Date()).endTime(new Date()).build());
        log.info(String.valueOf(libraryHots.size()));
    }

    @Test
    public void testHotSearchText() {
        StatisticsQueryDto  statisticsQueryDto= StatisticsQueryDto.builder().startTime(new Date()).endTime(new Date()).build();
        statisticsQueryDto.setCurrent(0);
        statisticsQueryDto.setPageSize(2);
        List<HotRankStatisticsEntity> hotSearchText = statisticsService.selectHotSearchText(statisticsQueryDto);
        log.info(String.valueOf(hotSearchText.size()));
    }

}
