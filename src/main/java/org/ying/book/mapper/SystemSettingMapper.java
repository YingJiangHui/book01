package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.SystemSetting;
import org.ying.book.pojo.SystemSettingExample;

public interface SystemSettingMapper {
    long countByExample(SystemSettingExample example);

    int deleteByExample(SystemSettingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SystemSetting row);

    int insertSelective(SystemSetting row);

    List<SystemSetting> selectByExampleWithRowbounds(SystemSettingExample example, RowBounds rowBounds);

    List<SystemSetting> selectByExample(SystemSettingExample example);

    SystemSetting selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") SystemSetting row, @Param("example") SystemSettingExample example);

    int updateByExample(@Param("row") SystemSetting row, @Param("example") SystemSettingExample example);

    int updateByPrimaryKeySelective(SystemSetting row);

    int updateByPrimaryKey(SystemSetting row);
}