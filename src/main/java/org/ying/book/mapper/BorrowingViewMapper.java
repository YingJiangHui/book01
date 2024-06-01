package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.BorrowingView;
import org.ying.book.pojo.BorrowingViewExample;

public interface BorrowingViewMapper {
    long countByExample(BorrowingViewExample example);

    int deleteByExample(BorrowingViewExample example);

    int insert(BorrowingView row);

    int insertSelective(BorrowingView row);

    List<BorrowingView> selectByExampleWithRowbounds(BorrowingViewExample example, RowBounds rowBounds);

    List<BorrowingView> selectByExample(BorrowingViewExample example);

    int updateByExampleSelective(@Param("row") BorrowingView row, @Param("example") BorrowingViewExample example);

    int updateByExample(@Param("row") BorrowingView row, @Param("example") BorrowingViewExample example);
}