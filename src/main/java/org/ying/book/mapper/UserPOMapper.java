package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.UserPO;
import org.ying.book.pojo.UserPOExample;

public interface UserPOMapper {
    long countByExample(UserPOExample example);

    int deleteByExample(UserPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserPO row);

    int insertSelective(UserPO row);

    List<UserPO> selectByExampleWithRowbounds(UserPOExample example, RowBounds rowBounds);

    List<UserPO> selectByExample(UserPOExample example);

    UserPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") UserPO row, @Param("example") UserPOExample example);

    int updateByExample(@Param("row") UserPO row, @Param("example") UserPOExample example);

    int updateByPrimaryKeySelective(UserPO row);

    int updateByPrimaryKey(UserPO row);
}