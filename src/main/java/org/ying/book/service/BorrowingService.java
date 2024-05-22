package org.ying.book.service;

import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.borrowing.BorrowingDto;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.BorrowingMapper;
import org.ying.book.pojo.Borrowing;
import org.ying.book.pojo.BorrowingExample;
import org.ying.book.pojo.Reservation;

import java.util.Date;
import java.util.List;

@Service
public class BorrowingService {

    @Resource
    BorrowingMapper borrowingMapper;

    @Resource
    ReservationService reservationService;

    //获取指定书籍在指定时间段下的借阅记录
    public List<Borrowing> getBorrowingsBetweenBorrowTime(List<Integer> bookIds, Date borrowedAt, Date returnedAt) {
        //        通过判断该书籍在对应时间段是否与其他借阅记录时间区间有冲突，以判断书籍是否可以借阅
        BorrowingExample borrowingExample = new BorrowingExample();
        BorrowingExample.Criteria criteria = borrowingExample.createCriteria();
        criteria.andBookIdIn(bookIds);
        criteria.andReturnedAtGreaterThan(borrowedAt);
        criteria.andBorrowedAtLessThan(returnedAt);

        return borrowingMapper.selectByExample(borrowingExample);
    }

    public void returnBooks(List<Integer> bookIds) {
        BorrowingExample borrowingExample = new BorrowingExample();
        BorrowingExample.Criteria criteria = borrowingExample.createCriteria();
        criteria.andBookIdIn(bookIds);
        List<Borrowing> borrowings = borrowingMapper.selectByExample(borrowingExample);
        borrowings.stream().map(borrowing -> {
            borrowing.setExpectedReturnAt(new Date());
            borrowingMapper.updateByPrimaryKeySelective(borrowing);
            return borrowing;
        }).toList();
    }

    @Transactional
    public List<Borrowing> borrowBooks(BorrowingDto borrowingDto) {
        List<Integer> bookIds = borrowingDto.getBookIds();
        List<Borrowing> borrowings = this.getBorrowingsBetweenBorrowTime(bookIds, borrowingDto.getBorrowedAt(), borrowingDto.getExpectedReturnAt());
        if (borrowings != null && !borrowings.isEmpty()) {
            throw new CustomException("该书籍在该时间段内已被借阅", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<Reservation> reservations = reservationService.getReservationsBetweenBorrowTime(bookIds, borrowingDto.getBorrowedAt(), borrowingDto.getExpectedReturnAt());
        if (reservations != null && !reservations.isEmpty()) {
            throw new CustomException("该书籍在该时间段内已被预约", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Integer userId = UserContext.getCurrentUser().getId();

        return bookIds.stream().map(bookId -> {
            Borrowing borrowing = Borrowing.builder()
                    .bookId(bookId)
                    .userId(userId)
                    .borrowedAt(borrowingDto.getBorrowedAt())
                    .borrowedAt(borrowingDto.getExpectedReturnAt())
                    .build();
            borrowingMapper.insertSelective(borrowing);
            return borrowing;
        }).toList();
    }

}
