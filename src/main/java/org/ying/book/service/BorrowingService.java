package org.ying.book.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.plugin.Intercepts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.borrowing.BorrowingDto;
import org.ying.book.dto.borrowing.BorrowingQueryDto;
import org.ying.book.dto.borrowing.RenewDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.enums.ActionSource;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.BorrowingMapper;
import org.ying.book.mapper.BorrowingViewMapper;
import org.ying.book.pojo.*;
import org.ying.book.utils.PaginationHelper;

import java.util.Date;
import java.util.List;

@Service
public class BorrowingService {

    @Value("${custom.book.borrowing.default-days}")
    private Integer defaultDays;

    @Value("${custom.book.borrowing.max-days}")
    private Integer maxDays;

    @Resource
    BorrowingMapper borrowingMapper;

    @Resource
    FileService fileService;

    @Resource
    BorrowingViewMapper borrowingViewMapper;

    @Resource
    ReservationService reservationService;

    @Resource
    BookShelfService bookShelfService;
    @Autowired
    private UserService userService;

    public boolean borrowDaysValidate(Date startDate, Date endDate, Integer additionDays){
        long days = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        return days >= 1 && days <= (maxDays - additionDays);
    }

    public boolean borrowDaysValidate(Date startDate, Date endDate){
        return this.borrowDaysValidate(startDate,endDate,0);
    }

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

            if(!this.borrowDaysValidate(borrowing.getBorrowedAt(), renewDto.getExpectedReturnAt())){
                throw new CustomException(String.format("超过了最大借阅天数$d天",maxDays));
            }

            borrowing.setExpectedReturnAt(renewDto.getExpectedReturnAt());
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
            if(borrowing.getExpectedReturnAt().getTime()<new Date().getTime()){
                userService.defaultTimesAddOne(borrowing.getUserId());
            }
            borrowing.setReturnedAt(new Date());
            borrowingMapper.updateByPrimaryKeySelective(borrowing);
            return borrowing;
        }).toList();
    }

    public void validConflictBorrowTime(List<Integer> bookIds, Date borrowedAt, Date returnedAt) {
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

    @Transactional
    public void borrowFromReservations(List<Integer> reservationIds) {
        List<Reservation> reservations = reservationService.getReservationsByIds(reservationIds);
        if (reservations.size() != reservationIds.size()) {
            throw new CustomException("部分书籍未被预约");
        }
        reservations.stream().map(reservation -> {
            if (reservation.getBorrowingId() != null) {
                throw new CustomException("书籍已被借阅");
            }
            Borrowing borrowing = Borrowing.builder()
                    .bookId(reservation.getBookId())
                    .userId(reservation.getUserId())
                    .borrowedAt(reservation.getBorrowedAt())
                    .expectedReturnAt(reservation.getReturnedAt())
                    .build();
            borrowingMapper.insertSelective(borrowing);
            reservation.setBorrowingId(borrowing.getId());
            return reservation;
        }).map((reservation) -> reservationService.finishReservations(reservation)).toList();
    }

    public PageResultDto<BorrowingView> getBorrowingsPaginate(BorrowingQueryDto borrowingQueryDto) {
        BorrowingViewExample borrowingViewExample = new BorrowingViewExample();
        BorrowingViewExample.Criteria criteria = borrowingViewExample.createCriteria();
        if (borrowingQueryDto.getUserId() != null) {
            criteria.andUserIdEqualTo(borrowingQueryDto.getUserId());
        }

        if (borrowingQueryDto.getStatus() != null && !borrowingQueryDto.getStatus().isEmpty()) {
            criteria.andStatusIn(borrowingQueryDto.getStatus().stream().map(Enum::name).toList());
        }

        borrowingViewExample.setOrderByClause("expected_return_at asc");
        return PaginationHelper.paginate(borrowingQueryDto, (rowBounds, reqDto) -> borrowingViewMapper.selectByExampleWithRowbounds(borrowingViewExample, rowBounds).stream().map((borrowingView)->{
            borrowingView.getBook().setFiles(fileService.filesWithUrl(borrowingView.getBook().getFiles()));
            borrowingView.setLatestReturnAt(new Date(borrowingView.getBorrowedAt().getTime() + 1000 * 60 * 60 * 24 * maxDays));
            return borrowingView;
        }).toList(), borrowingViewMapper.countByExample(borrowingViewExample));
    }
}
