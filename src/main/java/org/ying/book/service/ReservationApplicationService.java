package org.ying.book.service;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.borrowing.BorrowingDto;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.email.EmailBorrowNotificationDto;
import org.ying.book.dto.reservationApplication.ReservationApplicationQueryDto;
import org.ying.book.enums.ActionSource;
import org.ying.book.enums.ReservationApplicationEnum;
import org.ying.book.enums.ReservationStatusEnum;
import org.ying.book.enums.SystemSettingsEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.BorrowingViewMapper;
import org.ying.book.mapper.ReservationApplicationMapper;
import org.ying.book.pojo.*;
import org.ying.book.utils.PaginationHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationApplicationService.class);
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

    @Resource
    private ReservationApplicationService reservationApplicationService;
    @Autowired
    private LibraryService libraryService;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    public Boolean isRepetitionReservation(Integer userId, Integer BookId) {
        ReservationApplicationExample reservationApplicationExample = new ReservationApplicationExample();
        reservationApplicationExample.createCriteria().andUserIdEqualTo(userId).andBookIdEqualTo(BookId).andStatusIn(Arrays.asList(ReservationApplicationEnum.PENDING.name(), ReservationApplicationEnum.NOTIFIED.name()));
        return reservationApplicationMapper.countByExample(reservationApplicationExample) > 0;
    }

    @Transactional
    public ReservationApplication reservationApply(ReservationApplication reservationApplication) {

        if(reservationApplication.getBookId()!=null){
            Library library = libraryService.getLibraryByBookId(reservationApplication.getBookId());
            if(library.getDisableReserveApplication()){
                throw new CustomException("该图书馆已关闭预约功能");
            }
        }else{
            throw new CustomException("未找到书籍");
        }
        if (isRepetitionReservation(reservationApplication.getUserId(), reservationApplication.getBookId())) {
            throw new CustomException("不可重复预约", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(this.getFirstReservationApplication(reservationApplication.getBookId())!=null){
            reservationApplicationMapper.insertSelective(reservationApplication);
            return reservationApplication;
        }

        if (!borrowingService.hasBorrowed(reservationApplication.getBookId())) {
            throw new CustomException("未借阅的书籍不可申请预约", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        reservationApplicationMapper.insertSelective(reservationApplication);
        return reservationApplication;
    }

    private ReservationApplicationExample.Criteria withQueryExample(ReservationApplicationQueryDto reservationApplicationQueryDto, ReservationApplicationExample reservationApplicationExample) {
        ReservationApplicationExample.Criteria criteria = reservationApplicationExample.createCriteria();

        if (reservationApplicationQueryDto.getUserId() != null) {
            criteria.andUserIdEqualTo(reservationApplicationQueryDto.getUserId());
        }

        if (reservationApplicationQueryDto.getBookId() != null) {
            criteria.andBookIdEqualTo(reservationApplicationQueryDto.getBookId());
        }
        if (reservationApplicationQueryDto.getStatus() != null && !reservationApplicationQueryDto.getStatus().isEmpty()) {
            criteria.andStatusIn(reservationApplicationQueryDto.getStatus().stream().map(ReservationApplicationEnum::name).toList());
        }

        if (reservationApplicationQueryDto.getEmail() != null && !reservationApplicationQueryDto.getEmail().isEmpty()) {
            criteria.andEmailEqualTo(reservationApplicationQueryDto.getEmail());
        }

        if (reservationApplicationQueryDto.getTitle() != null && !reservationApplicationQueryDto.getTitle().isEmpty()) {
            criteria.andTitleLike("%" + reservationApplicationQueryDto.getTitle() + "%");
        }

        if (reservationApplicationQueryDto.getLibraryId() != null) {
            criteria.andLibraryIdEqualTo(reservationApplicationQueryDto.getLibraryId());
        }

        return criteria;
    }


    public List<ReservationApplication> getReservationApplicationList(ReservationApplicationQueryDto reservationApplicationQueryDto) {
        ReservationApplicationExample reservationApplicationExample = new ReservationApplicationExample();
        reservationApplicationExample.setOrderByClause(reservationApplicationQueryDto.getOrderByClause()!=null?reservationApplicationQueryDto.getOrderByClause():"created_at desc");
        withQueryExample(reservationApplicationQueryDto, reservationApplicationExample);
        return reservationApplicationMapper.selectByExample(reservationApplicationExample).stream().map((item) -> {
            item.getBook().setFiles(fileService.filesWithUrl(item.getBook().getFiles()));
            return item;
        }).toList();
    }

    public PageResultDto<ReservationApplication> getReservationApplicationListPagination(ReservationApplicationQueryDto reservationApplicationQueryDto) {
        ReservationApplicationExample reservationApplicationExample = new ReservationApplicationExample();
        reservationApplicationExample.setOrderByClause("created_at desc");

        withQueryExample(reservationApplicationQueryDto, reservationApplicationExample);
        return PaginationHelper.paginate(reservationApplicationQueryDto, (rowBounds, reqDto) -> reservationApplicationMapper.selectByExampleWithRowbounds(reservationApplicationExample, rowBounds).stream().map((item) -> {
            item.getBook().setFiles(fileService.filesWithUrl(item.getBook().getFiles()));
            return item;
        }).toList(), reservationApplicationMapper.countByExample(reservationApplicationExample));
    }

    public ReservationApplication getFirstReservationApplication(Integer bookId) {
        ReservationApplicationQueryDto reservationApplicationQueryDto = ReservationApplicationQueryDto.builder().bookId(bookId).orderByClause("created_at asc").status(Arrays.asList(ReservationApplicationEnum.PENDING, ReservationApplicationEnum.NOTIFIED)).build();
        List<ReservationApplication> reservationApplications = getReservationApplicationList(reservationApplicationQueryDto);
        if (reservationApplications.isEmpty()) {
            return null;
        }
        return reservationApplications.get(0);
    }

    public ReservationApplication getFirstPendingReservationApplication(Integer bookId) {
        ReservationApplicationQueryDto reservationApplicationQueryDto = ReservationApplicationQueryDto.builder().bookId(bookId).orderByClause("created_at asc").status(Arrays.asList(ReservationApplicationEnum.PENDING)).build();
        List<ReservationApplication> reservationApplications = getReservationApplicationList(reservationApplicationQueryDto);
        if (reservationApplications.isEmpty()) {
            return null;
        }
        return reservationApplications.get(0);
    }

    @Transactional
    public void cancelReservationApplication(Integer reservationApplicationId) {
        ReservationApplication reservationApplication = reservationApplicationMapper.selectByPrimaryKey(reservationApplicationId);
        String prevStatus = reservationApplication.getStatus();
        reservationApplicationService.cancelReservationApplication(reservationApplication);
        if(Objects.equals(prevStatus, "NOTIFIED")){
            try {
                ReservationApplication firstReservationApplication = reservationApplicationService.getFirstPendingReservationApplication(reservationApplication.getBookId());
                if (firstReservationApplication != null)
                    reservationApplicationService.notifiedReservationApplication(firstReservationApplication.getId());
            } catch (Exception e) {
                log.info("借阅通知发送失败");
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelReservationApplication(ReservationApplication reservationApplication){
        if (reservationApplication.getStatus().equals(ReservationApplicationEnum.CANCELLED.name())) {
            throw new CustomException("预约申请已取消", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (reservationApplication.getStatus().equals(ReservationApplicationEnum.FULFILLED.name())) {
            throw new CustomException("预约申请已完成", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        reservationApplication.setStatus(ReservationApplicationEnum.CANCELLED.name());
        reservationApplicationMapper.updateByPrimaryKeySelective(reservationApplication);
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

        if (reservationApplication == null || reservationApplication.getStatus() == ReservationApplicationEnum.CANCELLED.name() || reservationApplication.getStatus() == ReservationApplicationEnum.FULFILLED.name()) {
            throw new CustomException("不满足通知条件", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        reservationApplicationService.sendNotification(reservationApplication);
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

    @Transactional
    public void resendNotification(Integer reservationApplicationId) throws MessagingException {
        ReservationApplication reservationApplication = reservationApplicationMapper.selectByPrimaryKey(reservationApplicationId);
        if (reservationApplication == null) {
            throw new CustomException("预约申请不存在", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!Objects.equals(reservationApplication.getStatus(), ReservationApplicationEnum.NOTIFIED.name())) {
            throw new CustomException("不满足通知条件", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        reservationApplicationService.notifiedReservationApplication(reservationApplication.getId());


    }

    public List<ReservationApplication> getCurrentReservedBook(Integer bookId){
        ReservationApplicationExample reservationApplicationExample = new ReservationApplicationExample();
        reservationApplicationExample.createCriteria().andBookIdEqualTo(bookId).andStatusIn(Arrays.asList(ReservationApplicationEnum.NOTIFIED.name(), ReservationApplicationEnum.PENDING.name()));
        return reservationApplicationMapper.selectByExample(reservationApplicationExample);
    }

}
