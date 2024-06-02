package org.ying.book.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.ying.book.enums.SearchTargetEnum;
import org.ying.book.mapper.SearchMapper;
import org.ying.book.pojo.Search;
import org.ying.book.pojo.SearchExample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchHistoryService {
    @Resource
    private SearchMapper searchMapper;

    public List<Search> getSearchHistory(Integer userId, String keyword, SearchTargetEnum target) {
//        SearchExample searchExample = new SearchExample();
//        searchExample.createCriteria().andUserIdEqualTo(userId).andKeywordLike(keyword).andTargetEqualTo(target.name());
        if(target==null){
            throw new RuntimeException("target is null");
        }
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("keyword", keyword);
        map.put("target", target.name());
        return searchMapper.selectByUserIdAndTargetAndKeyword(map);
    }

    public Search saveSearchHistory(Integer userId, String keyword, SearchTargetEnum target) {
        Search search = Search.builder().userId(userId).keyword(keyword).target(target.name()).build();
        searchMapper.insertSelective(search);
        return search;
    }
}
