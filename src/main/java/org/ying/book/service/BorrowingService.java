package org.ying.book.service;

import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.Context.UserContext;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.BorrowingMapper;
import org.ying.book.pojo.Borrowing;
import org.ying.book.pojo.BorrowingExample;

import java.util.Date;
import java.util.List;

@Service
public class BorrowingService {

    @Resource
    BorrowingMapper borrowingMapper;

//获取指定书籍在指定时间段下的借阅记录
    public List<Borrowing> getBorrowingBetweenBorrowTime(Integer bookId, Date borrowedAt, Date returnedAt) {
        //        通过判断该书籍在对应时间段是否与其他借阅记录时间区间有冲突，以判断书籍是否可以借阅
        BorrowingExample borrowingExample = new BorrowingExample();
        BorrowingExample.Criteria criteria = borrowingExample.createCriteria();
        criteria.andReturnedAtGreaterThan(borrowedAt);
        criteria.andBorrowedAtLessThan(returnedAt);

        return borrowingMapper.selectByExample(borrowingExample);
    }

    @Transactional
    public Borrowing borrowBook(Borrowing borrowing) {
//        通过判断该书籍在对应时间段是否与其他借阅记录时间区间有冲突，以判断书籍是否可以借阅
        BorrowingExample borrowingExample = new BorrowingExample();
        BorrowingExample.Criteria criteria = borrowingExample.createCriteria();
        List<Borrowing> borrowings = getBorrowingBetweenBorrowTime(borrowing.getBookId(), borrowing.getBorrowedAt(), borrowing.getReturnedAt());
        if (borrowings == null || !borrowings.isEmpty()) {
            throw new CustomException("该书籍在该时间段内已被借阅", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Integer userId = UserContext.getCurrentUser().getId();
        borrowing.setUserId(userId);
        borrowingMapper.insertSelective(borrowing);
        return borrowing;
    }

}
