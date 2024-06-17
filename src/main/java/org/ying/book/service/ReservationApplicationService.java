package org.ying.book.service;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.borrowing.BorrowingDto;
import org.ying.book.dto.email.EmailBorrowNotificationDto;
import org.ying.book.dto.reservationApplication.ReservationApplicationQueryDto;
import org.ying.book.enums.ActionSource;
import org.ying.book.enums.ReservationApplicationEnum;
import org.ying.book.enums.SystemSettingsEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.BorrowingViewMapper;
import org.ying.book.mapper.ReservationApplicationMapper;
import org.ying.book.pojo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ReservationApplicationService {

    @Resource
    ReservationApplicationMapper reservationApplicationMapper;

    @Resource
    BorrowingViewMapper borrowingViewMapper;

    @Resource
    BorrowingService borrowingService;

    @Resource
    EmailService emailService;
    @Autowired
    private BookService bookService;
    @Autowired
    private SystemSettingsService systemSettingsService;

    @Resource
    private FileService fileService;

    public Boolean isRepetitionReservation(Integer userId, Integer BookId){
        ReservationApplicationExample reservationApplicationExample = new ReservationApplicationExample();
        reservationApplicationExample.createCriteria().andUserIdEqualTo(userId).andBookIdEqualTo(BookId).andStatusIn(Arrays.asList(ReservationApplicationEnum.PENDING.name(), ReservationApplicationEnum.NOTIFIED.name()));
        return reservationApplicationMapper.countByExample(reservationApplicationExample)>0;
    }

    @Transactional
    public ReservationApplication reservationApply(ReservationApplication reservationApplication) {
        if (!borrowingService.hasBorrowed(reservationApplication.getBookId())) {
            throw new CustomException("未借阅的书籍不可申请预定", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (isRepetitionReservation(reservationApplication.getUserId(), reservationApplication.getBookId())) {
            throw new CustomException("不可重复预定", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        reservationApplicationMapper.insertSelective(reservationApplication);
        return reservationApplication;
    }

    public List<ReservationApplication> getReservationApplicationList(ReservationApplicationQueryDto reservationApplicationQueryDto) {
        ReservationApplicationExample reservationApplicationExample = new ReservationApplicationExample();
        reservationApplicationExample.setOrderByClause("created_at asc");
        ReservationApplicationExample.Criteria criteria = reservationApplicationExample.createCriteria();
        if(reservationApplicationQueryDto.getBookId()!=null){
            criteria.andBookIdEqualTo(reservationApplicationQueryDto.getBookId());
        }
        if (reservationApplicationQueryDto.getStatus() != null && !reservationApplicationQueryDto.getStatus().isEmpty()) {
            criteria.andStatusIn(reservationApplicationQueryDto.getStatus().stream().map(ReservationApplicationEnum::name).toList());
        }
        return reservationApplicationMapper.selectByExample(reservationApplicationExample).stream().map((item)->{
            item.getBook().setFiles(fileService.filesWithUrl(item.getBook().getFiles()));
            return item;
        }).toList();
    }
    public ReservationApplication getFirstReservationApplication(Integer bookId) {
        ReservationApplicationQueryDto reservationApplicationQueryDto = ReservationApplicationQueryDto.builder().bookId(bookId).status(Arrays.asList(ReservationApplicationEnum.PENDING, ReservationApplicationEnum.NOTIFIED)).build();
        List<ReservationApplication> reservationApplications = getReservationApplicationList(reservationApplicationQueryDto);
        if (reservationApplications.isEmpty()) {
            return null;
        }
        return reservationApplications.get(0);
    }
    @Transactional
    public void cancelReservationApplication(Integer reservationApplicationId) {
        ReservationApplication reservationApplication = reservationApplicationMapper.selectByPrimaryKey(reservationApplicationId);
        if (reservationApplication.getStatus().equals(ReservationApplicationEnum.CANCELLED.name())) {
            throw new CustomException("预定申请已取消", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (reservationApplication.getStatus().equals(ReservationApplicationEnum.FULFILLED.name())) {
            throw new CustomException("预定申请已完成", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        reservationApplication.setStatus(ReservationApplicationEnum.CANCELLED.name());
        reservationApplicationMapper.deleteByPrimaryKey(reservationApplicationId);
    }

    @Transactional
    public void fulfillReservationApplication(Integer reservationApplicationId) {
        ReservationApplication reservationApplication = reservationApplicationMapper.selectByPrimaryKey(reservationApplicationId);
        if (!reservationApplication.getStatus().equals(ReservationApplicationEnum.NOTIFIED.name())) {
            throw new CustomException("此书籍无法借阅", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        BorrowingDto borrowingDto = new BorrowingDto();
        borrowingDto.setUserId(reservationApplication.getUserId());
        borrowingDto.setBookIds(Arrays.asList(reservationApplication.getBookId()));
        borrowingDto.setBorrowedAt(new Date());
        borrowingDto.setFrom(ActionSource.RESERVATION);

        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 获取系统设置的借阅天数
        int defaultBorrowDays = Integer.parseInt(systemSettingsService.getSystemSettingValueByName(SystemSettingsEnum.DEFAULT_BORROW_DAYS).toString());
        // 添加defaultBorrowDays天
        LocalDate futureDate = today.plusDays(defaultBorrowDays);
        // 设置时间为23:59:59
        LocalDateTime futureDateTime = futureDate.atTime(LocalTime.MAX);
        Date futureDateAsDate = Date.from(futureDateTime.atZone(ZoneId.systemDefault()).toInstant());
        borrowingDto.setExpectedReturnAt(futureDateAsDate);

        List<Borrowing> borrowings = borrowingService.borrowBooks(borrowingDto);
        reservationApplication.setStatus(ReservationApplicationEnum.FULFILLED.name());
        reservationApplication.setBorrowingId(borrowings.isEmpty() ? null : borrowings.get(0).getId());
        reservationApplicationMapper.updateByPrimaryKeySelective(reservationApplication);
    }
    @Transactional
    public void notifiedReservationApplication(Integer reservationApplicationId) throws MessagingException {
        ReservationApplication reservationApplication = reservationApplicationMapper.selectByPrimaryKey(reservationApplicationId);

        if (reservationApplication==null||reservationApplication.getStatus()==ReservationApplicationEnum.CANCELLED.name()||reservationApplication.getStatus()==ReservationApplicationEnum.FULFILLED.name()) {
            throw new CustomException("不满足通知条件", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        sendNotification(reservationApplication);
        reservationApplication.setStatus(ReservationApplicationEnum.NOTIFIED.name());
        reservationApplicationMapper.updateByPrimaryKeySelective(reservationApplication);
    }

    @Transactional
    public void sendNotification(ReservationApplication reservationApplication) throws MessagingException {
        Integer days = Integer.parseInt(systemSettingsService.getSystemSettingValueByName(SystemSettingsEnum.GET_BOOK_DAYS).toString());

        reservationApplication.setStatus(ReservationApplicationEnum.NOTIFIED.name());
        bookService.getBook(reservationApplication.getBookId());
        emailService.sendNotificationEmail(EmailBorrowNotificationDto.builder()
                .email(reservationApplication.getUser().getEmail())
                .bookName(reservationApplication.getBook().getTitle())
                .days(days)
                .libraryName(reservationApplication.getBook().getLibrary().getName())
                .build());
    }

}