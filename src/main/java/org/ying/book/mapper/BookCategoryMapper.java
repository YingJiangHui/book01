package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.BookCategory;
import org.ying.book.pojo.BookCategoryExample;

public interface BookCategoryMapper {
    long countByExample(BookCategoryExample example);

    int deleteByExample(BookCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BookCategory row);

    int insertSelective(BookCategory row);

    List<BookCategory> selectByExampleWithRowbounds(BookCategoryExample example, RowBounds rowBounds);

    List<BookCategory> selectByExample(BookCategoryExample example);

    BookCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") BookCategory row, @Param("example") BookCategoryExample example);

    int updateByExample(@Param("row") BookCategory row, @Param("example") BookCategoryExample example);

    int updateByPrimaryKeySelective(BookCategory row);

    int updateByPrimaryKey(BookCategory row);
}