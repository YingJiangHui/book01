package org.ying.book.service;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.ying.book.dto.email.EmailBorrowNotificationDto;
import org.ying.book.enums.ReservationApplicationEnum;
import org.ying.book.enums.SystemSettingsEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.BorrowingMapper;
import org.ying.book.mapper.BorrowingViewMapper;
import org.ying.book.mapper.ReservationApplicationMapper;
import org.ying.book.mapper.ReservationMapper;
import org.ying.book.pojo.BorrowingView;
import org.ying.book.pojo.BorrowingViewExample;
import org.ying.book.pojo.ReservationApplication;
import org.ying.book.pojo.ReservationApplicationExample;

import java.util.Arrays;
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


    public ReservationApplication reservationApply(ReservationApplication reservationApplication) {
        if (!borrowingService.hasBorrowed(reservationApplication.getBookId())) {
            throw new CustomException("未借阅的书籍不可申请预约", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        reservationApplicationMapper.insertSelective(reservationApplication);
        return reservationApplication;
    }

    public List<ReservationApplication> getReservationApplicationList(Integer bookId, List<ReservationApplicationEnum> statusList) {
        ReservationApplicationExample reservationApplicationExample = new ReservationApplicationExample();
        reservationApplicationExample.setOrderByClause("created_at asc");
        reservationApplicationExample.createCriteria().andBookIdEqualTo(bookId);
        if (statusList != null && !statusList.isEmpty()) {
            reservationApplicationExample.createCriteria().andStatusIn(statusList.stream().map(ReservationApplicationEnum::name).toList());
        }
        return reservationApplicationMapper.selectByExample(reservationApplicationExample);
    }

    public ReservationApplication getFirstReservationApplication(Integer bookId) {
        List<ReservationApplication> reservationApplications = getReservationApplicationList(bookId, Arrays.asList(ReservationApplicationEnum.PENDING, ReservationApplicationEnum.NOTIFIED));
        if (reservationApplications.isEmpty()) {
            return null;
        }
        return reservationApplications.get(0);
    }

    public void cancelReservationApplication(Integer reservationApplicationId) {
        ReservationApplication reservationApplication = reservationApplicationMapper.selectByPrimaryKey(reservationApplicationId);
        if (reservationApplication.getStatus().equals(ReservationApplicationEnum.CANCELLED.name())) {
            throw new CustomException("预约申请已取消", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (reservationApplication.getStatus().equals(ReservationApplicationEnum.FULFILL.name())) {
            throw new CustomException("预约申请已完成", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        reservationApplication.setStatus(ReservationApplicationEnum.CANCELLED.name());
        reservationApplicationMapper.deleteByPrimaryKey(reservationApplicationId);
    }

    public void fulfillReservationApplication(Integer reservationApplicationId, Integer borrowingId) {
        ReservationApplication reservationApplication = reservationApplicationMapper.selectByPrimaryKey(reservationApplicationId);
        if (!reservationApplication.getStatus().equals(ReservationApplicationEnum.NOTIFIED.name())) {
            throw new CustomException("无法完成借阅", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        reservationApplication.setStatus(ReservationApplicationEnum.FULFILL.name());
        reservationApplication.setBorrowingId(borrowingId);
        reservationApplicationMapper.updateByPrimaryKeySelective(reservationApplication);
    }

    public void notifiedReservationApplication(Integer reservationApplicationId) throws MessagingException {
        ReservationApplication reservationApplication = reservationApplicationMapper.selectByPrimaryKey(reservationApplicationId);
        if (!Arrays.asList(ReservationApplicationEnum.PENDING.name(),ReservationApplicationEnum.NOTIFIED.name()).contains(reservationApplication.getStatus())) {
            throw new CustomException("不满足通知条件", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        sendNotification(reservationApplication);
        reservationApplication.setStatus(ReservationApplicationEnum.NOTIFIED.name());
        reservationApplicationMapper.updateByPrimaryKeySelective(reservationApplication);
    }

    private void sendNotification(ReservationApplication reservationApplication) throws MessagingException {
        Integer days = (Integer) systemSettingsService.getSystemSettingValueByName(SystemSettingsEnum.GET_BOOK_DAYS);
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
