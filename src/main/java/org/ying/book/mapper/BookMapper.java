package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.Book;
import org.ying.book.pojo.BookExample;

public interface BookMapper {
    long countByExample(BookExample example);

    int deleteByExample(BookExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Book row);

    int insertSelective(Book row);

    List<Book> selectByExampleWithRowbounds(BookExample example, RowBounds rowBounds);

    List<Book> selectByExample(BookExample example);

    Book selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Book row, @Param("example") BookExample example);

    int updateByExample(@Param("row") Book row, @Param("example") BookExample example);

    int updateByPrimaryKeySelective(Book row);

    int updateByPrimaryKey(Book row);
}