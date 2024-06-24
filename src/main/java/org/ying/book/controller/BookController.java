package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.book.BookDto;
import org.ying.book.dto.book.BookQueryDto;
import org.ying.book.dto.book.BookSearchDto;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.user.UserJwtDto;
import org.ying.book.enums.SearchTargetEnum;
import org.ying.book.pojo.Book;
import org.ying.book.service.BookService;
import org.ying.book.service.FileService;
import org.ying.book.service.SearchHistoryService;

import java.io.InputStream;
import java.net.URLConnection;

@RestController
@RequestMapping(value = "/books")
public class BookController {

    @Resource
    private BookService bookService;
    @Resource
    private FileService fileService;

    @Resource
    private SearchHistoryService searchHistoryService;

    @GetMapping
    public PageResultDto<Book> getBooksPagination(@ModelAttribute BookQueryDto bookQueryDto) {
//        InputStream in = fileService.getFile("2024-05/20/9b5d5f64-95dc-49b6-a0ba-100c4d3857e3.png");
//        String contentTypeString = URLConnection.guessContentTypeFromStream(in);
//        if (contentTypeString == null) {
//            contentTypeString = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Use a default content type
//        }
//        MediaType contentType = MediaType.parseMediaType(contentTypeString);
//        return ResponseEntity.ok().contentType(contentType).body(in.readAllBytes());
        return bookService.getBooksPagination(bookQueryDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Book createBook(@ModelAttribute BookDto bookDto) throws Exception {
        return bookService.createBook(bookDto);
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Integer id) {
        return bookService.getBook(id);
    }

    @GetMapping("/search")
    public PageResultDto<Book> searchBooks(BookSearchDto bookSearchDto) {
        PageResultDto<Book> bookPageResultDto = bookService.searchBook(bookSearchDto);
        UserJwtDto currentUser = UserContext.getCurrentUser();
        if (bookPageResultDto.getTotal() != 0 && currentUser != null) {

            searchHistoryService.saveSearchHistory(currentUser.getId(), bookSearchDto.getKeyword(), SearchTargetEnum.BOOK);
        }
        return bookPageResultDto;
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") Integer id) {
        bookService.deleteBook(id);
    }
    @PostMapping("/{id}")
    public void recoverBook(@PathVariable("id") Integer id) {
        bookService.recoverBook(id);
    }

    @PatchMapping(value ="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateBook(@PathVariable("id") Integer id, @ModelAttribute BookDto bookDto) {
        bookService.updateBook(id, bookDto);
    }
}
