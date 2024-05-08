package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.Library;
import org.ying.book.pojo.LibraryExample;

public interface LibraryMapper {
    long countByExample(LibraryExample example);

    int deleteByExample(LibraryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Library row);

    int insertSelective(Library row);

    List<Library> selectByExampleWithRowbounds(LibraryExample example, RowBounds rowBounds);

    List<Library> selectByExample(LibraryExample example);

    Library selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Library row, @Param("example") LibraryExample example);

    int updateByExample(@Param("row") Library row, @Param("example") LibraryExample example);

    int updateByPrimaryKeySelective(Library row);

    int updateByPrimaryKey(Library row);
}