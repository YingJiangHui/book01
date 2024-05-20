package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.BookFile;
import org.ying.book.pojo.BookFileExample;

public interface BookFileMapper {
    long countByExample(BookFileExample example);

    int deleteByExample(BookFileExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BookFile row);

    int insertSelective(BookFile row);

    List<BookFile> selectByExampleWithRowbounds(BookFileExample example, RowBounds rowBounds);

    List<BookFile> selectByExample(BookFileExample example);

    BookFile selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") BookFile row, @Param("example") BookFileExample example);

    int updateByExample(@Param("row") BookFile row, @Param("example") BookFileExample example);

    int updateByPrimaryKeySelective(BookFile row);

    int updateByPrimaryKey(BookFile row);
}