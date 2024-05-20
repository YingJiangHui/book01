package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.File;
import org.ying.book.pojo.FileExample;

public interface FileMapper {
    long countByExample(FileExample example);

    int deleteByExample(FileExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(File row);

    int insertSelective(File row);

    List<File> selectByExampleWithRowbounds(FileExample example, RowBounds rowBounds);

    List<File> selectByExample(FileExample example);

    File selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") File row, @Param("example") FileExample example);

    int updateByExample(@Param("row") File row, @Param("example") FileExample example);

    int updateByPrimaryKeySelective(File row);

    int updateByPrimaryKey(File row);
}