package org.ying.book.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.Role;
import org.ying.book.pojo.User;
import org.ying.book.pojo.UserExample;

public interface UserMapper{
    long countByExample(UserExample example);
    long countByExampleWithRoleAndLibrary(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User row);

    int insertSelective(User row);

    List<User> selectByExampleWithRowbounds(UserExample example, RowBounds rowBounds);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") User row, @Param("example") UserExample example);

    int updateByExample(@Param("row") User row, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    User selectUserByEmail(String userEmail);

    List<Role> selectRoleByUserId(int userId);
    List<User> selectByExampleWithRoleNameAndLibraryAndRowbounds(UserExample example, RowBounds rowBounds);
}