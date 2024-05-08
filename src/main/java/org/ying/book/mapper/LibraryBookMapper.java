package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.LibraryBook;
import org.ying.book.pojo.LibraryBookExample;

public interface LibraryBookMapper {
    long countByExample(LibraryBookExample example);

    int deleteByExample(LibraryBookExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LibraryBook row);

    int insertSelective(LibraryBook row);

    List<LibraryBook> selectByExampleWithRowbounds(LibraryBookExample example, RowBounds rowBounds);

    List<LibraryBook> selectByExample(LibraryBookExample example);

    LibraryBook selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") LibraryBook row, @Param("example") LibraryBookExample example);

    int updateByExample(@Param("row") LibraryBook row, @Param("example") LibraryBookExample example);

    int updateByPrimaryKeySelective(LibraryBook row);

    int updateByPrimaryKey(LibraryBook row);
}