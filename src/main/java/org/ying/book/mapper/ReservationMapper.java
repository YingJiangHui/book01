package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.Reservation;
import org.ying.book.pojo.ReservationExample;

public interface ReservationMapper {
    long countByExample(ReservationExample example);

    int deleteByExample(ReservationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Reservation row);

    int insertSelective(Reservation row);

    List<Reservation> selectByExampleWithRowbounds(ReservationExample example, RowBounds rowBounds);

    List<Reservation> selectByExample(ReservationExample example);

    Reservation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Reservation row, @Param("example") ReservationExample example);

    int updateByExample(@Param("row") Reservation row, @Param("example") ReservationExample example);

    int updateByPrimaryKeySelective(Reservation row);

    int updateByPrimaryKey(Reservation row);
}