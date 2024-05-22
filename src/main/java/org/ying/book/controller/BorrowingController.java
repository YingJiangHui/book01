package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ying.book.dto.borrowing.BorrowingDto;
import org.ying.book.pojo.Borrowing;
import org.ying.book.service.BorrowingService;

import java.util.List;

@RestController
@RequestMapping(value = "/borrowing")
public class BorrowingController {

    @Resource
    private BorrowingService borrowingService;

    @PostMapping
    public List<Borrowing> borrowBooks(@RequestBody BorrowingDto borrowingDto) {
        return borrowingService.borrowBooks(borrowingDto);
    }


}
