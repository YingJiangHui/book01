package org.ying.book.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.ying.book.mapper.BookMapper;
import org.ying.book.pojo.Book;

@Service
public class BookService {

    @Resource
    private BookMapper bookMapper;

    public Book createBook(Book book){
        bookMapper.insertSelective(book);
        return null;
    }
}
