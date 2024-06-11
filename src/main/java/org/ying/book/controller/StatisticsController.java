package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ying.book.dto.statistics.StatisticsQueryDto;
import org.ying.book.pojo.HotRankStatisticsEntity;
import org.ying.book.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    StatisticsService statisticsService;

    @RequestMapping("/hot-borrowed-books")
    public List<HotRankStatisticsEntity> hotBorrowedBooks(@ModelAttribute StatisticsQueryDto statisticsQueryDto) {
        return statisticsService.getHotBorrowedBooks(statisticsQueryDto);
    }


    @RequestMapping("/hot-borrowed-categories")
    public List<HotRankStatisticsEntity> hotBorrowedCategories(@ModelAttribute StatisticsQueryDto statisticsQueryDto) {
        return statisticsService.getHotBorrowedCategories(statisticsQueryDto);
    }

    @RequestMapping("/hot-borrowed-libraries")
    public List<HotRankStatisticsEntity> hotBorrowedLibraries(@ModelAttribute StatisticsQueryDto statisticsQueryDto) {
        return statisticsService.getHotBorrowedLibraries(statisticsQueryDto);
    }

    @RequestMapping("/hot-search-text")
    public List<HotRankStatisticsEntity> hotSearchText(@ModelAttribute StatisticsQueryDto statisticsQueryDto) {
        return statisticsService.selectHotSearchText(statisticsQueryDto);
    }


}
