package org.ying.book.service;

import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.reservation.ReservationDto;
import org.ying.book.enums.ActionSource;
import org.ying.book.exception.CustomException;
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

    @Resource
    BorrowingService borrowingService;

    @Resource
    BookShelfService bookShelfService;

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

    public void validConflictReserveTime(List<Integer> bookIds, Date borrowedAt, Date returnedAt) {
        List<Reservation> reservations = getReservationsBetweenBorrowTime(bookIds, borrowedAt, returnedAt);
        if (reservations != null && !reservations.isEmpty()) {
            throw new CustomException("书籍在该时间段内已被预约", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public List<Reservation> reserveBooks(ReservationDto reservationDto) {
        List<Integer> bookIds = reservationDto.getBookIds();

        borrowingService.validConflictBorrowTime(bookIds, reservationDto.getBorrowedAt(), reservationDto.getExpectedReturnAt());
        this.validConflictReserveTime(bookIds, reservationDto.getBorrowedAt(), reservationDto.getExpectedReturnAt());

        List<Reservation> reservations = bookIds.stream().map(bookId -> {
            Reservation reservation = Reservation.builder()
                    .bookId(bookId)
                    .userId(reservationDto.getUserId())
                    .borrowedAt(reservationDto.getBorrowedAt())
                    .returnedAt(reservationDto.getExpectedReturnAt())
                    .build();
            reservationMapper.insertSelective(reservation);
            return reservation;
        }).toList();

        if (reservationDto.getFrom().equals(ActionSource.SHELF)) {
            bookShelfService.removeBooksFromShelfByUserIdAndBookIds(reservationDto.getUserId(), bookIds);
        }

        return reservations;
    }
}
