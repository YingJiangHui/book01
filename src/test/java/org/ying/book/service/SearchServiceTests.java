package org.ying.book.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.ying.book.enums.SearchTargetEnum;
import org.ying.book.pojo.Search;

import java.util.List;

@Slf4j
@SpringBootTest
public class SearchServiceTests {

    @Resource
    private SearchHistoryService searchService;
    @Test
    public void test() {
        List<Search> searchList = searchService.getSearchHistory(2, "test", SearchTargetEnum.BOOK);
        log.info("test");
        searchList.forEach(Search::toString);
    }

    @Test
    public void test2() {
        Search search = searchService.saveSearchHistory(2, "test", SearchTargetEnum.BOOK);
        log.info("test2");
        log.info(search.toString());
    }
}
