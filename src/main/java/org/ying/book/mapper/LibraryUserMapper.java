package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.LibraryUser;
import org.ying.book.pojo.LibraryUserExample;

public interface LibraryUserMapper {
    long countByExample(LibraryUserExample example);

    int deleteByExample(LibraryUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LibraryUser row);

    int insertSelective(LibraryUser row);

    List<LibraryUser> selectByExampleWithRowbounds(LibraryUserExample example, RowBounds rowBounds);

    List<LibraryUser> selectByExample(LibraryUserExample example);

    LibraryUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") LibraryUser row, @Param("example") LibraryUserExample example);

    int updateByExample(@Param("row") LibraryUser row, @Param("example") LibraryUserExample example);

    int updateByPrimaryKeySelective(LibraryUser row);

    int updateByPrimaryKey(LibraryUser row);
}