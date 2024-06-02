package org.ying.book.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.ying.book.dto.book.BookSearchDto;
import org.ying.book.dto.common.PageResultDto;

@Slf4j
@SpringBootTest
public class BookServiceTests {
    @Resource
    BookService bookService;

    @Test
    public void test() {
        BookSearchDto bookSearchDto = new BookSearchDto();
        bookSearchDto.setKeyword("杰克");
        bookSearchDto.setLibraryId(1);
        PageResultDto books1 = bookService.searchBook(bookSearchDto);
        log.info("books1: {}", books1);
//        bookSearchDto.setLibraryId(1);
//
//        PageResultDto books2 = bookService.searchBook(bookSearchDto);
//        log.info("books1: {}", books1);
    }
}
