package org.ying.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.ying.book.pojo.ReservationApplication;
import org.ying.book.pojo.ReservationApplicationExample;

public interface ReservationApplicationMapper {
    long countByExample(ReservationApplicationExample example);

    int deleteByExample(ReservationApplicationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ReservationApplication row);

    int insertSelective(ReservationApplication row);

    List<ReservationApplication> selectByExampleWithRowbounds(ReservationApplicationExample example, RowBounds rowBounds);

    List<ReservationApplication> selectByExample(ReservationApplicationExample example);

    ReservationApplication selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") ReservationApplication row, @Param("example") ReservationApplicationExample example);

    int updateByExample(@Param("row") ReservationApplication row, @Param("example") ReservationApplicationExample example);

    int updateByPrimaryKeySelective(ReservationApplication row);

    int updateByPrimaryKey(ReservationApplication row);
}