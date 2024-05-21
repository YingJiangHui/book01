package org.ying.book.service;

import org.springframework.stereotype.Service;
import org.ying.book.mapper.BorrowingMapper;
import org.ying.book.pojo.Borrowing;

@Service
public class BorrowingService {

    BorrowingMapper borrowingMapper;
    public void borrowBook(Borrowing borrowing) {
        borrowingMapper.insertSelective(borrowing);
    }

}
