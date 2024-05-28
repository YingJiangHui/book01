package org.ying.book.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.plugin.Intercepts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.borrowing.BorrowingDto;
import org.ying.book.dto.borrowing.RenewDto;
import org.ying.book.enums.ActionSource;
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

    @Resource
    BookShelfService bookShelfService;

    //获取指定书籍在指定时间段下的借阅记录
    public List<Borrowing> getBorrowingsBetweenBorrowTime(List<Integer> bookIds, Date borrowedAt, Date returnedAt) {
        //        通过判断该书籍在对应时间段是否与其他借阅记录时间区间有冲突，以判断书籍是否可以借阅
        BorrowingExample borrowingExample = new BorrowingExample();
//        已经归还的图书
        BorrowingExample.Criteria criteria = borrowingExample.createCriteria();
        criteria.andBookIdIn(bookIds)
                .andReturnedAtIsNotNull()
                .andReturnedAtGreaterThan(borrowedAt)
                .andBorrowedAtLessThan(returnedAt);
//        未归还的图书
        BorrowingExample.Criteria criteria2 = borrowingExample.createCriteria();
        criteria2.andBookIdIn(bookIds)
                .andReturnedAtIsNull()
                .andExpectedReturnAtGreaterThan(borrowedAt)
                .andBorrowedAtLessThan(returnedAt);
        borrowingExample.or(criteria2);
        return borrowingMapper.selectByExample(borrowingExample);
    }

    //    续借操作
    @Transactional
    public void renewBooks(RenewDto renewDto) {
        BorrowingExample borrowingExample = new BorrowingExample();
        BorrowingExample.Criteria criteria = borrowingExample.createCriteria();
        criteria.andIdIn(renewDto.getBorrowingIds());
        List<Borrowing> borrowings = borrowingMapper.selectByExample(borrowingExample);
        borrowings.stream().map(borrowing -> {
            if (borrowing.getReturnedAt() != null) {
                throw new CustomException("该书籍已归还，无法续借", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            borrowing.setExpectedReturnAt(new Date(borrowing.getExpectedReturnAt().getTime() + 1000 * 60 * 60 * 24 * renewDto.getDays()));
            borrowingMapper.updateByPrimaryKeySelective(borrowing);
            return borrowing;
        }).toList();
    }

    @Transactional
    public void returnBooks(List<Integer> borrowingIds) {
        BorrowingExample borrowingExample = new BorrowingExample();
        BorrowingExample.Criteria criteria = borrowingExample.createCriteria();
        criteria.andIdIn(borrowingIds);
        List<Borrowing> borrowings = borrowingMapper.selectByExample(borrowingExample);
        borrowings.stream().map(borrowing -> {
            borrowing.setBorrowedAt(new Date());
            borrowingMapper.updateByPrimaryKeySelective(borrowing);
            return borrowing;
        }).toList();
    }

    public void validConflictBorrowTime(List<Integer> bookIds, Date borrowedAt, Date returnedAt){
        List<Borrowing> borrowings = this.getBorrowingsBetweenBorrowTime(bookIds, borrowedAt, returnedAt);
        if (borrowings != null && !borrowings.isEmpty()) {
            throw new CustomException("书籍在该时间段内已被借阅", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public List<Borrowing> borrowBooks(BorrowingDto borrowingDto) {
        List<Integer> bookIds = borrowingDto.getBookIds();

        this.validConflictBorrowTime(bookIds, borrowingDto.getBorrowedAt(), borrowingDto.getExpectedReturnAt());
        reservationService.validConflictReserveTime(bookIds, borrowingDto.getBorrowedAt(), borrowingDto.getExpectedReturnAt());

        List<Borrowing> borrowings = bookIds.stream().map(bookId -> {
            Borrowing borrowing = Borrowing.builder()
                    .bookId(bookId)
                    .userId(borrowingDto.getUserId())
                    .borrowedAt(borrowingDto.getBorrowedAt())
                    .expectedReturnAt(borrowingDto.getExpectedReturnAt())
                    .build();
            borrowingMapper.insertSelective(borrowing);
            return borrowing;
        }).toList();

        if (borrowingDto.getFrom().equals(ActionSource.SHELF)) {
            bookShelfService.removeBooksFromShelfByUserIdAndBookIds(borrowingDto.getUserId(), bookIds);
        }
        return borrowings;
    }

}
