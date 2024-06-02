package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.user.UserJwtDto;
import org.ying.book.enums.SearchTargetEnum;
import org.ying.book.pojo.Search;
import org.ying.book.service.SearchHistoryService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/search-history")
public class SearchHistoryController {

    @Resource
    private SearchHistoryService searchHistoryService;

    @GetMapping
    public List<Search> getSearchHistory(@ModelAttribute Search search) {
        UserJwtDto userJwtDto = UserContext.getCurrentUser();
        search.setUserId(userJwtDto.getId());
        return searchHistoryService.getSearchHistory(search);
    }

    @GetMapping("/all")
    public List<Search> getSearchHistoryAll(@ModelAttribute Search search) {
        if(Objects.equals(search.getKeyword(), "")){
            return Arrays.asList();
        }
        return searchHistoryService.getSearchHistory(search);
    }
}
