package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.BookShelf;
import org.ying.book.pojo.BookShelfExample;

public interface BookShelfMapper {
    long countByExample(BookShelfExample example);

    int deleteByExample(BookShelfExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BookShelf row);

    int insertSelective(BookShelf row);

    List<BookShelf> selectByExampleWithRowbounds(BookShelfExample example, RowBounds rowBounds);

    List<BookShelf> selectByExample(BookShelfExample example);

    BookShelf selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") BookShelf row, @Param("example") BookShelfExample example);

    int updateByExample(@Param("row") BookShelf row, @Param("example") BookShelfExample example);

    int updateByPrimaryKeySelective(BookShelf row);

    int updateByPrimaryKey(BookShelf row);

}