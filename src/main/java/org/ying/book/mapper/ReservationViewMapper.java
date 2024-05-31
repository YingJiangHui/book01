package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.ReservationView;
import org.ying.book.pojo.ReservationViewExample;

public interface ReservationViewMapper {
    long countByExample(ReservationViewExample example);

    int deleteByExample(ReservationViewExample example);

    int insert(ReservationView row);

    int insertSelective(ReservationView row);

    List<ReservationView> selectByExampleWithRowbounds(ReservationViewExample example, RowBounds rowBounds);

    List<ReservationView> selectByExample(ReservationViewExample example);

    int updateByExampleSelective(@Param("row") ReservationView row, @Param("example") ReservationViewExample example);

    int updateByExample(@Param("row") ReservationView row, @Param("example") ReservationViewExample example);
}