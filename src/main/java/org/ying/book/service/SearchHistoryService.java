package org.ying.book.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.ying.book.dto.search.SearchDto;
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

    public List<Search> getSearchHistory(SearchDto search) {
        SearchExample searchExample = new SearchExample();

        SearchExample.Criteria criteria = searchExample.createCriteria();
        if (search.getUserId() != null) {
            criteria.andUserIdEqualTo(search.getUserId());
        }
        if (search.getTarget() != null) {
            criteria.andTargetEqualTo(search.getTarget());
        }
        if (search.getKeyword() != null) {
            criteria.andKeywordLike("%" + search.getKeyword() + "%");
        }
        RowBounds rowBounds = search.getSize() == null ? new RowBounds() : new RowBounds(0, search.getSize());

        return searchMapper.selectByUserIdAndTargetAndKeywordWithRowbounds(searchExample, rowBounds);
    }


    public Search saveSearchHistory(Integer userId, String keyword, SearchTargetEnum target) {
        Search search = Search.builder().userId(userId).keyword(keyword).target(target.name()).build();
        searchMapper.insertSelective(search);
        return search;
    }
}
