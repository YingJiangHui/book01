package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.Role;
import org.ying.book.pojo.RoleExample;

public interface RoleMapper {
    long countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Role row);

    int insertSelective(Role row);

    List<Role> selectByExampleWithRowbounds(RoleExample example, RowBounds rowBounds);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Role row, @Param("example") RoleExample example);

    int updateByExample(@Param("row") Role row, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role row);

    int updateByPrimaryKey(Role row);

    Role selectByRoleName(String roleName);
}