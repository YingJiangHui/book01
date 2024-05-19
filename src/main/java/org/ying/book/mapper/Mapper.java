package org.ying.book.mapper;

import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.User;
import org.ying.book.pojo.UserExample;

import java.util.List;

public interface Mapper<T,E> {
    List<T> selectByExampleWithRowbounds(E example, RowBounds rowBounds);
    long countByExample(E example);
}
