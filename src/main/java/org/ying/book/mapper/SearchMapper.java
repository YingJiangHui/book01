package org.ying.book.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.Search;
import org.ying.book.pojo.SearchExample;

public interface SearchMapper {
    long countByExample(SearchExample example);

    int deleteByExample(SearchExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Search row);

    int insertSelective(Search row);

    List<Search> selectByExampleWithRowbounds(SearchExample example, RowBounds rowBounds);

    List<Search> selectByExample(SearchExample example);

    Search selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Search row, @Param("example") SearchExample example);

    int updateByExample(@Param("row") Search row, @Param("example") SearchExample example);

    int updateByPrimaryKeySelective(Search row);

    int updateByPrimaryKey(Search row);

    List<Search> selectByUserIdAndTargetAndKeyword(SearchExample example);

    List<Search> selectByUserIdAndTargetAndKeywordWithRowbounds(SearchExample example, RowBounds rowBounds);
}