package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.borrowing.BorrowingDto;
import org.ying.book.dto.borrowing.RenewDto;
import org.ying.book.exception.CustomException;
import org.ying.book.pojo.Borrowing;
import org.ying.book.service.BorrowingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/books/borrowing")
public class BorrowingController {

    @Resource
    private BorrowingService borrowingService;

    @PostMapping
    public List<Borrowing> borrowBooks(@RequestBody BorrowingDto borrowingDto) {
        if(UserContext.getCurrentUser() == null || UserContext.getCurrentUser().getId() == null){
            throw new CustomException("用户未登录", HttpStatus.UNAUTHORIZED);
        }
        borrowingDto.setUserId(UserContext.getCurrentUser().getId());
        return borrowingService.borrowBooks(borrowingDto);
    }


    @PostMapping(value = "/renew")
    public void renewBooks(@RequestBody RenewDto renewDto) {
        borrowingService.renewBooks(renewDto);
    }

    @PostMapping(value = "/return")
    public void returnBooks(@RequestBody List<Integer> borrowingIds) {
        borrowingService.returnBooks(borrowingIds);
    }

    @PostMapping("/reservations")
    public void borrowBooksFormReservations(@RequestBody List<Integer> reservationIds) {
        borrowingService.borrowFromReservations(reservationIds);
    }
}
