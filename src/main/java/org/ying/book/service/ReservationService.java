package org.ying.book.service;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.reservation.ReservationDto;
import org.ying.book.dto.reservation.ReservationQueryDto;
import org.ying.book.dto.reservation.ReservationQueryWithPageDto;
import org.ying.book.enums.ActionSource;
import org.ying.book.enums.ReservationStatusEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.ReservationMapper;
import org.ying.book.mapper.ReservationViewMapper;
import org.ying.book.pojo.*;
import org.ying.book.utils.PaginationHelper;

import java.util.*;

@Service
public class ReservationService {
    @Resource
    ReservationViewMapper reservationViewMapper;

    @Resource
    ReservationMapper reservationMapper;

    @Resource
    BorrowingService borrowingService;

    @Resource
    BookShelfService bookShelfService;

    @Resource
    FileService fileService;
    @Autowired
    private LibraryService libraryService;

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
            throw new CustomException("书籍在该时间段内已被预订", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void reserveBooksV2(){

    }

    @Transactional
    public List<Reservation> reserveBooks(ReservationDto reservationDto) {
        List<Integer> bookIds = reservationDto.getBookIds();

        if(bookIds!=null&&!bookIds.isEmpty()){
            Library library = libraryService.getLibraryByBookId(bookIds.get(0));
            if(library.getDisableReserve()){
                throw new CustomException("该图书馆已关闭预订功能");
            }
        }else{
            throw new CustomException("未找到书籍");
        }

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

    public List<Reservation> getReservationsByIds(List<Integer> ids) {
        ReservationExample reservationExample = new ReservationExample();
        ReservationExample.Criteria criteria = reservationExample.createCriteria();
        if(ids==null || ids.isEmpty()){
            return new ArrayList<>();
        }
        criteria.andIdIn(ids);
        criteria.andDeletedNotEqualTo(true);
        List<Reservation> reservations = reservationMapper.selectByExample(reservationExample);
        return reservations;
    }

    @Transactional
    public List<Reservation> cancelReservations(List<Integer> ids) {
        List<Reservation> reservations = getReservationsByIds(ids);
        if(reservations.isEmpty()){
            throw new CustomException("未找到预订记录");
        }
        if (ids.size() != reservations.size()) {
            throw new CustomException("部分书籍未被预订");
        }

        reservations.forEach((reservation) -> {
            if (reservation.getBorrowingId() != null) {
                throw new CustomException("书籍已被借阅");
            }
        });
        reservations.forEach((reservationView) -> {
            Reservation reservation = Reservation.builder().id(reservationView.getId()).deleted(true).build();
            reservationMapper.updateByPrimaryKeySelective(reservation);
        });
        return reservations;
    }

    @Transactional
    public Reservation finishReservations(Reservation reservation, Integer borrowingId) {
        reservation.setBorrowingId(borrowingId);
        reservationMapper.updateByPrimaryKeySelective(reservation);
        return reservation;
//        List<Reservation> reservations = getReservationsByIds(ids);
//        if(reservations.isEmpty()){
//            throw new CustomException("未找到预约记录");
//        }
//        if (ids.size() != reservations.size()) {
//            throw new CustomException("部分书籍未被预约");
//        }
//        reservations.forEach((reservation) -> {
//            reservation.setStatus(ReservationStatusEnum.FULFILLED);
//            reservationMapper.updateByPrimaryKey(reservation);
//        });
//        return reservations;
    }

    public List<ReservationView> getReservations(ReservationQueryDto reservationQueryDto){
        ReservationViewExample reservationExample = new ReservationViewExample();
//        按照创建日期倒序
        reservationExample.setOrderByClause("created_at desc");
//        将status == BORROWABLE的图书顺序提前
//        reservationExample.setOrderByClause("status desc");

        ReservationViewExample.Criteria criteria = reservationExample.createCriteria();
        if(reservationQueryDto.getUserId() != null){
            criteria.andUserIdEqualTo(reservationQueryDto.getUserId());
        }
        if(reservationQueryDto.getBookId() != null){
            criteria.andBookIdEqualTo(reservationQueryDto.getBookId());
        }
        Map<String, Integer> statusOrder = new HashMap<>();
        statusOrder.put(ReservationStatusEnum.BORROWABLE.name(), 1);
        statusOrder.put(ReservationStatusEnum.NOT_BORROWABLE.name(), 2);
        statusOrder.put(ReservationStatusEnum.EXPIRED.name(), 3);
        statusOrder.put(ReservationStatusEnum.FULFILLED.name(), 4);
        statusOrder.put(ReservationStatusEnum.CANCELLED.name(), 5);


        return reservationViewMapper.selectByExample(reservationExample).stream().map((item)-> {
            item.getBook().setFiles(fileService.filesWithUrl(item.getBook().getFiles()));
            return item;
        }).sorted(Comparator.comparing((ReservationView rv) ->
                statusOrder.get(rv.getStatus())
        )).toList();
    }

    public PageResultDto<ReservationView> getReservationPagination(ReservationQueryWithPageDto reservationQueryDto){
        ReservationViewExample reservationExample = new ReservationViewExample();
//        按照创建日期倒序
        reservationExample.setOrderByClause("created_at desc");
//        将status == BORROWABLE的图书顺序提前
//        reservationExample.setOrderByClause("status desc");

        ReservationViewExample.Criteria criteria = reservationExample.createCriteria();
        if(reservationQueryDto.getTitle() != null){
            criteria.andTitleLike("%"+reservationQueryDto.getTitle()+"%");
        }
        if(reservationQueryDto.getLibraryId() != null){
            criteria.andLibraryIdEqualTo(reservationQueryDto.getLibraryId());
        }
        if(reservationQueryDto.getEmail() != null && !reservationQueryDto.getEmail().isEmpty()){
            criteria.andEmailEqualTo(reservationQueryDto.getEmail().trim());
        }
        if(reservationQueryDto.getId() != null){
            criteria.andIdEqualTo(reservationQueryDto.getId());
        }
        if(reservationQueryDto.getBookId() != null){
            criteria.andBookIdEqualTo(reservationQueryDto.getBookId());
        }
        if(reservationQueryDto.getStatus() != null && !reservationQueryDto.getStatus().isEmpty()){
            criteria.andStatusEqualTo(reservationQueryDto.getStatus());
        }

        return PaginationHelper.paginate(reservationQueryDto, (rowBounds, reqDto) -> reservationViewMapper.selectByExampleWithRowbounds(reservationExample, rowBounds), reservationViewMapper.countByExample(reservationExample));
    }

    public List<ReservationView> getCurrentReservedBook(Integer bookId){
        ReservationViewExample reservationViewExample = new ReservationViewExample();
        reservationViewExample.createCriteria().andBookIdEqualTo(bookId).andStatusIn(Arrays.asList(ReservationStatusEnum.NOT_BORROWABLE.name(), ReservationStatusEnum.BORROWABLE.name()));
        return reservationViewMapper.selectByExample(reservationViewExample);
    }
}
