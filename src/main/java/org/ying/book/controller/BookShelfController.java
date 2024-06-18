package org.ying.book.controller;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.bookShelf.BookInShelfDto;
import org.ying.book.dto.bookShelf.BookShelfGroupByLibraryDto;
import org.ying.book.pojo.BookShelf;
import org.ying.book.service.BookShelfService;

import java.util.List;

@RestController
@RequestMapping(value = "/book-shelf")
public class BookShelfController {

    @Resource
    private BookShelfService bookShelfService;

    @PostMapping
    public void addBookToShelf(@RequestBody BookShelf bookShelf) {
        bookShelf.setUserId(UserContext.getCurrentUser().getId());
        bookShelfService.addBookToShelf(bookShelf);
    }

    @GetMapping
    public List<BookShelfGroupByLibraryDto> getBookShelf() {
        return bookShelfService.getBookShelfByUserId(UserContext.getCurrentUser().getId());
    }

    @DeleteMapping
    public List<BookShelf> deleteBookFromShelf(@RequestBody List<Integer> bookIds) {
        return bookShelfService.removeBooksFromShelf(bookIds,UserContext.getCurrentUser().getId());
    }
}
