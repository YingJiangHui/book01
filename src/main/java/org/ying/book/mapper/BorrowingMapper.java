package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.*;

public interface BorrowingMapper {
    long countByExample(BorrowingExample example);

    int deleteByExample(BorrowingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Borrowing row);

    int insertSelective(Borrowing row);

    List<Borrowing> selectByExampleWithRowbounds(BorrowingExample example, RowBounds rowBounds);

    List<Borrowing> selectByExample(BorrowingExample example);

    Borrowing selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Borrowing row, @Param("example") BorrowingExample example);

    int updateByExample(@Param("row") Borrowing row, @Param("example") BorrowingExample example);

    int updateByPrimaryKeySelective(Borrowing row);

    int updateByPrimaryKey(Borrowing row);

    List<HotRankStatisticsEntity> selectHotBorrowedBooks(BorrowingExample example, RowBounds rowBounds);
    List<HotRankStatisticsEntity> selectHotBorrowedCategories(BorrowingExample example, RowBounds rowBounds);
    List<HotRankStatisticsEntity> selectHotBorrowedLibraries(@Param("example") BorrowingExample example,@Param("dateTrunc") String dateTrunc, RowBounds rowBounds);
}