package org.ying.book.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.ying.book.mapper.ReservationMapper;
import org.ying.book.pojo.Borrowing;
import org.ying.book.pojo.BorrowingExample;
import org.ying.book.pojo.Reservation;
import org.ying.book.pojo.ReservationExample;

import java.util.Date;
import java.util.List;

@Service
public class ReservationService {

    @Resource
    ReservationMapper reservationMapper;

    //获取指定书籍在指定时间段下的借阅记录
    public List<Reservation> getReservationsBetweenBorrowTime(List<Integer> bookIds, Date borrowedAt, Date returnedAt) {
        //        通过判断该书籍在对应时间段是否与其他借阅记录时间区间有冲突，以判断书籍是否可以借阅
        ReservationExample borrowingExample = new ReservationExample();
        ReservationExample.Criteria criteria = borrowingExample.createCriteria();
        criteria.andBookIdIn(bookIds);
        criteria.andReturnedAtGreaterThan(borrowedAt);
        criteria.andBorrowedAtLessThan(returnedAt);
        return reservationMapper.selectByExample(borrowingExample);
    }
}
