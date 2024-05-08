package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.UserRole;
import org.ying.book.pojo.UserRoleExample;

public interface UserRoleMapper {
    long countByExample(UserRoleExample example);

    int deleteByExample(UserRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserRole row);

    int insertSelective(UserRole row);

    List<UserRole> selectByExampleWithRowbounds(UserRoleExample example, RowBounds rowBounds);

    List<UserRole> selectByExample(UserRoleExample example);

    UserRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") UserRole row, @Param("example") UserRoleExample example);

    int updateByExample(@Param("row") UserRole row, @Param("example") UserRoleExample example);

    int updateByPrimaryKeySelective(UserRole row);

    int updateByPrimaryKey(UserRole row);
}