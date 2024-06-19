package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.borrowing.BorrowingDto;
import org.ying.book.dto.borrowing.BorrowingQueryDto;
import org.ying.book.dto.borrowing.RenewDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.user.UserJwtDto;
import org.ying.book.enums.RoleEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.pojo.Borrowing;
import org.ying.book.pojo.BorrowingView;
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

    @GetMapping()
    public PageResultDto<BorrowingView> getBorrowings(@ModelAttribute BorrowingQueryDto borrowingQueryDto) {
        UserJwtDto userJwtDto = UserContext.getCurrentUser();
//        如果是用户进行查询
        borrowingQueryDto.setUserId(userJwtDto.getId());

        return borrowingService.getBorrowingsPaginate(borrowingQueryDto);
    }

    @GetMapping("/all")
    public PageResultDto<BorrowingView> getBorrowingsAll(@ModelAttribute BorrowingQueryDto borrowingQueryDto) {
        return borrowingService.getBorrowingsPaginate(borrowingQueryDto);
    }
}
